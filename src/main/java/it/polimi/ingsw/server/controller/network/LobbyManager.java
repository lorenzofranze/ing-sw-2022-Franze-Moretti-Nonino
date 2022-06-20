package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.Server.Controller.Network.PingSender;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyManager implements Runnable {
    private Map<GameMode, Lobby> waitingLobbies;
    private ServerController serverController;
    private ServerSocket lobbyServerSocket;
    private Socket clientSocket;
    private int lobbyPortNumber=32502;
    private List<String> disconnectedPlayers;
    private static LobbyManager lobbyManager = null;


    /**
     * There is only one serverController used to manage the new connections. When a player is accepted,
     * the player interacts with one of the executor's servers
     *
     * @param lobbyPortNumber
     */

    private LobbyManager() {
        System.out.println("porta server 32501");
        boolean portAvailable=false;
        this.waitingLobbies = new HashMap<>();
        this.serverController = ServerController.getInstance();
        this.disconnectedPlayers = new ArrayList<>();



        while(!portAvailable) {
            portAvailable=true;
            try {
                lobbyServerSocket = new ServerSocket(lobbyPortNumber);
            } catch (IOException e) {
                System.err.println("port not available " + e.getMessage()); //port not available
                System.out.println("porta cambiata in quella successiva");
                this.lobbyPortNumber++;
                portAvailable=false;
            }
        }
        return;

    }

    public static LobbyManager getInstance() {
        if (lobbyManager == null) {
            lobbyManager = new LobbyManager();
        }
        return lobbyManager;
    }

    public void getStarted() {
        try {
            this.welcomeNewPlayers();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /** the server listen for clients. One client aat time is listened. The client connects with a
     * connection-message telling its nickname and its preferred game-mode.
     * A buffer in reader is created.
     * It is read the first message and it is checked the validity of the nickname(no other connected-players
     * can have the same nickname)
     * If the nickname is validated, it is added to the waiting players' list in the Lobby
     * @throws IOException
     */
    public void welcomeNewPlayers() throws IOException {
        Message unknown;
        boolean nameOk;
        System.out.println("Server ready on port: " + this.lobbyPortNumber);
        while (true) {
            try {
                this.clientSocket = lobbyServerSocket.accept();
                Thread t = new Thread(this);
                t.start();

            } catch (IOException e) {
                break; //In case the serverSocket gets closed
            }
        }
        lobbyServerSocket.close();
    }

    /**
     * Opens the buffer in and out,
     * ---> if the message received is of type connection, reads the nickname and sends a message
     * to inform about the correct connection or of the error due to the nickname already in use.
     * ---> if the message received is not of type connection, sets playerManager.setConnected(true) to
     * inform the PingSender about the alive-connection
     */
    @Override
    public void run(){
        Message unknown;
        JsonConverter jsonConverter = new JsonConverter();
        ArrayList<String> usedNicknames = new ArrayList<>();
        boolean validName;
        Message message;
        String stringMessage;
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error in client IO");
            return;
        }
        PlayerManager playerManager=new PlayerManager(in, out);

        validName = false;
        while (!validName) {
            String words = "";
            String line = null;
            try {
                line = in.readLine();
                while (!("EOF").equals(line)) {
                    words = words + line + "\n";
                    line = in.readLine();
                }
            } catch (IOException e){
                System.out.println("client has disconnected");
                return;
            }

            //System.out.println(words);
            jsonConverter = new JsonConverter();
            unknown = jsonConverter.fromJsonToMessage(words);

            if (unknown.getMessageType() == TypeOfMessage.Connection) {
                System.out.println(words);
                ConnectionMessage firstMessage = (ConnectionMessage) unknown;
                String nickname = firstMessage.getNickname();
                ConnectionMessage connectionMessage = (ConnectionMessage) firstMessage;

                /**
                 * Check of the nickname-univocity usinf a list of used nicknames.
                 */
                //check playing-clients
                if (!disconnectedPlayers.contains(nickname)) {
                    usedNicknames.clear();
                    for (int i : serverController.getInstance().getCurrentGames().keySet()) {
                        for (Player p : ServerController.getInstance().getCurrentGames().get(i).getGame().getPlayers()) {
                            usedNicknames.add(p.getNickname());
                        }
                    }
                    //check waiting-clients
                    for (GameMode gameMode : GameMode.values()) {
                        for (Lobby lobby : waitingLobbies.values()) {
                            usedNicknames.addAll(lobby.getUsersNicknames());
                        }
                    }

                    if (!usedNicknames.contains(nickname)) {
                        GameMode gameMode = connectionMessage.getGameMode();
                        System.out.println(nickname + " si è connesso");
                        AckMessage messageAck = new AckMessage(TypeOfAck.CorrectConnection);
                        addNickname(nickname, gameMode, clientSocket, playerManager);
                        validName = true;
                        playerManager.setPlayerNickname(nickname);
                        playerManager.getPingSender().setPlayerNickname(nickname);
                        try {
                            stringMessage = JsonConverter.fromMessageToJson(messageAck);
                            out.write(stringMessage);
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ErrorMessage errorMessage = new ErrorMessage(TypeOfError.UsedName);
                        validName = false;
                        try {
                            stringMessage = JsonConverter.fromMessageToJson(errorMessage);
                            out.write(stringMessage);
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //RESILIENZA ALLE DISCONNESSIONI
                /*
                else {

                    usedNicknames.remove(nickname);
                    for (int i : serverController.getInstance().getCurrentGames().keySet()) {
                        for (Player p : ServerController.getInstance().getCurrentGames().get(i).getGame().getPlayers()) {
                            if(p.getNickname()==nickname){
                                PlayerManager playerManager=
                                        ServerController.getInstance().getCurrentGames().get(i).getMessageHandler().getPlayerManager(nickname);
                                playerManager.getPingSender().setConnected(true);
                                /**todo**/ //gli invio l'update?
                           /* }
                        }
                     */
                    //si è riconnesso



            }else if(unknown.getMessageType() == TypeOfMessage.Ping){
                System.out.println("ping ricevuto, invio pong");
                PongMessage pongMessage = new PongMessage();
                try {
                    stringMessage = JsonConverter.fromMessageToJson(pongMessage);
                    out.write(stringMessage);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR-LobbyManager-2");
                }
            }else if(unknown.getMessageType() == TypeOfMessage.Pong){
                System.out.println("pong ricevuto");
                playerManager.setConnected(true);
            }
        else{
                System.out.println("ERROR-LobbyManager-1: message received \n" + words);
            }
        }
    }
    //In case the serverSocket gets closed ( the break statement is called )


    /**
     * it add the nickname to the correct Lobby list and, if the list has gained the right number of players:
     * 1) a new game controller is created
     * 2) it gives a thread to the new game controller
     * 3) the lobby is removed because the players can start to play and they don't wait anymore
     *
     * @param nickname
     * @param mode
     * @param clientSocket
     */
    public void addNickname(String nickname, GameMode mode, Socket clientSocket, PlayerManager playerManager) {
        if (waitingLobbies.containsKey(mode)) {
            waitingLobbies.get(mode).addUsersReadyToPlay(nickname, clientSocket, playerManager);
            if (waitingLobbies.get(mode).getUsersReadyToPlay().size() == mode.getNumPlayers()) {
                serverController.startGame(waitingLobbies.get(mode));
                waitingLobbies.remove(mode);
            }
        } else {
            Lobby newLobby = new Lobby(mode);
            newLobby.addUsersReadyToPlay(nickname, clientSocket, playerManager);
            waitingLobbies.put(mode, newLobby);
        }
    }

    //RESILIENZA ALLE DISCONNESSIONI

    public void addDisconnectedPlayers(String disconnectedPlayer) {
        this.disconnectedPlayers.add(disconnectedPlayer);
    }

}


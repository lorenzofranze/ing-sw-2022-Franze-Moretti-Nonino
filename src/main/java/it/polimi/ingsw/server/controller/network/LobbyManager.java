package it.polimi.ingsw.server.controller.network;

/**
 * The server listen for clients.
 * When the lobbyServerSocket accept new connections, welcomeNewPlayers creates a thread of Lobby manager and
 * starts it. The thread manage the connection of the new player. If receives a message of type
 * --> Ping: manages the connection sending pong
 * --> Pong: it means correct connection with the still-"invalid nickname" player
 * --> Connection: checks the nickname-univocity
 * If the nickname isn't correct, sends an ErrorMessage and waits for another nickname
 * Otherwise, sets nickname to the PingSender-class and calls addNickname():
 * --> if the lobby of the correct type does not exists, it creates it,
 * --> it adds the player to the lobby
 * --> if the lobby gets full, calls serverController.startGame(waitingLobbies.get(mode)) and removes the lobby from
 *      the list of waiting lobbies
 */

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
    private int lobbyPortNumber;
    private List<String> disconnectedPlayers;
    private static LobbyManager lobbyManager = null;



    private LobbyManager(int lobbyPortNumber) {
        System.out.println("porta server "+lobbyPortNumber);
        boolean portAvailable=false;
        this.waitingLobbies = new HashMap<>();
        this.serverController = ServerController.getInstance();
        this.disconnectedPlayers = new ArrayList<>();

        while(!portAvailable) {
            portAvailable=true;
            try {
                lobbyServerSocket = new ServerSocket(lobbyPortNumber);
            } catch (IOException e) {
                System.err.println("port not available "); //port not available
                this.lobbyPortNumber++;
                portAvailable=false;
            }
        }
        return;

    }

    public static LobbyManager getInstance(int lobbyPortNumber) {
        if (lobbyManager == null) {
            lobbyManager = new LobbyManager(lobbyPortNumber);
        }
        return lobbyManager;
    }


    /** can be called only after lobbyManager first creation **/
    public static LobbyManager getInstance(){
        return lobbyManager;
    }

    public void getStarted() {
        try {
            this.welcomeNewPlayers();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /** The server listen for clients.
     * When the lobbyServerSocket accept new connections, welcomeNewPlayers creates a thread of Lobby manager and
     * starts it.
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

            jsonConverter = new JsonConverter();
            unknown = jsonConverter.fromJsonToMessage(words);

            if (unknown.getMessageType() == TypeOfMessage.Connection) {
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

            }else if(unknown.getMessageType() == TypeOfMessage.Ping){
                PongMessage pongMessage = new PongMessage();
                try {
                    stringMessage = JsonConverter.fromMessageToJson(pongMessage);
                    out.write(stringMessage);
                    out.flush();
                } catch (IOException e) {
                    System.out.println("ERROR-LobbyManager-2");
                }
            }else if(unknown.getMessageType() == TypeOfMessage.Pong){
                playerManager.setConnected(true);
            }
        else{
                System.out.println("ERROR-LobbyManager-1: message received \n" + words);
            }
        }
    }


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

    public Map<GameMode, Lobby> getWaitingLobbies() {
        return waitingLobbies;
    }
}


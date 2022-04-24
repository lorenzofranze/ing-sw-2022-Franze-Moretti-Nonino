package it.polimi.ingsw.Server.Controller.Network;

import it.polimi.ingsw.Server.Controller.GameMode;
import it.polimi.ingsw.Server.Controller.Network.Messages.*;
import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Model.Player;
import jdk.net.ExtendedSocketOptions;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LobbyManager implements Runnable {
    private Map<GameMode, Lobby> waitingLobbies;
    private ServerController serverController;
    private ServerSocket lobbyServerSocket;
    private Socket clientSocket;
    private int lobbyPortNumber = 32501;
    private List<String> disconnectedPlayers;
    private static LobbyManager lobbyManager = null;

    /**
     * there is only one serverController used to manage the new connections. When a player is accepted,
     * the player interacts with one of the executor's servers
     *
     * @param lobbyPortNumber
     */

    private LobbyManager() {

        this.waitingLobbies = new HashMap<>();
        this.serverController = ServerController.getInstance();
        this.disconnectedPlayers = new ArrayList<>();


            try {
                lobbyServerSocket = new ServerSocket(lobbyPortNumber);
            } catch (IOException e) {
                System.err.println(e.getMessage()); //port not available
                return;
            }

    }

    public static LobbyManager getInstance() {
        if (lobbyManager == null) {
            lobbyManager = new LobbyManager();
        }
        return lobbyManager;
    }

    public void run() {
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

    /**
     * ATTENTION: POSSIBLE ATTACK FROM A MALITIOUS CLIENT: never sends its nickname: i have to put a timeout
     * otherwise no other players can connect and start a new game
     */
    public void welcomeNewPlayers() throws IOException {
        JsonConverter jsonConverter = new JsonConverter();
        ArrayList<String> usedNicknames = new ArrayList<>();
        Message unknown;
        System.out.println("Server ready");
        while (true) {
            try {
                clientSocket = lobbyServerSocket.accept();
                clientSocket.setKeepAlive(true);
                System.out.println("nuova connessione accettata");

                /*The value of this socket option is an Integer that is the number of seconds of idle time before
                keep-alive initiates a probe.*/
                //socket.setOption(ExtendedSocketOptions.TCP_KEEPIDLE, 60000);  //60000 milliseconds = 1 minute

                /*The value of this socket option is an Integer that is the number of seconds to wait before
                retransmitting a keep-alive probe.*/
                //socket.setOption(ExtendedSocketOptions.TCP_KEEPINTERVAL, 3000); //30 seconds

                /*The value of this socket option is an Integer that is the maximum number of keep-alive probes to be sent.*/
                //socket.setOption(ExtendedSocketOptions.TCP_KEEPCOUNT, 3);

            } catch (IOException e) {
                break; //In case the serverSocket gets closed
            }

            BufferedReader in = null;
            BufferedWriter out = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }


            String words="";
            int ind=0;
            String tmp="";
            while(ind<4) {
                tmp=in.readLine();
                words = words+tmp;
                System.out.println("tmp"+ tmp);
                System.out.println(words);
                ind++;
            }

            System.out.println(words);

            System.out.println("flag");
            words=words+"}";
            unknown = jsonConverter.fromJsonToMessage(words);
            System.out.println(unknown);
            if (unknown.getMessageType() == TypeOfMessage.Connection) {
                ConnectionMessage firstMessage=(ConnectionMessage) unknown;
                String nickname = firstMessage.getNickname();
                ConnectionMessage connectionMessage = (ConnectionMessage) firstMessage;

                /**
                 * Check of the nickname-univocity
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
                        addNickname(nickname, gameMode, clientSocket);
                        System.out.println(nickname+" si è connesso");
                        out.write(1);
                        out.flush();

                    } else{
                        // to client: -1 if name isn't available
                        out.write(1);
                        out.flush();
                    }
                } else {
                    usedNicknames.remove(nickname);
                    //si è riconnesso
                    //GESTIRE
                }

            }else{
                System.out.println("sono nell'else");
            }
        }
        //In case the serverSocket gets closed ( the break statement is called )

        lobbyServerSocket.close();
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
    public void addNickname(String nickname, GameMode mode, Socket clientSocket) {
        if (waitingLobbies.containsKey(mode)) {
            waitingLobbies.get(mode).addUsersReadyToPlay(nickname, clientSocket);

            if (waitingLobbies.get(mode).getUsersReadyToPlay().size() == mode.getNumPlayers()) {
                serverController.setToStart(waitingLobbies.get(mode));
                waitingLobbies.remove(mode);
            }
        } else {
            Lobby newLobby = new Lobby(mode);
            newLobby.addUsersReadyToPlay(nickname, clientSocket);
            waitingLobbies.put(mode, newLobby);
        }
    }

    public void addDisconnectedPlayers(String disconnectedPlayer) {
        this.disconnectedPlayers.add(disconnectedPlayer);
    }
}


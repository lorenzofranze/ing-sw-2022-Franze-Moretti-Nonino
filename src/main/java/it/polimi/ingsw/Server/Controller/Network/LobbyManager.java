package it.polimi.ingsw.Server.Controller.Network;

import it.polimi.ingsw.Server.Controller.GameMode;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ConnectionMessage;
import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private int lobbyPortNumber = 5000;
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
        this.lobbyPortNumber = lobbyPortNumber;
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
        ServerMessage firstMessage;
        System.out.println("Server ready");
        Socket clientSocket = null;
        while (true) {
            try {
                Socket socket = lobbyServerSocket.accept();
            } catch (IOException e) {
                break; //In case the serverSocket gets closed
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            firstMessage = (ServerMessage) jsonConverter.fromJsonToMessage(in.readLine());
            if (firstMessage.getMessageType() == "ConnectionMessage") {
                String nickname = firstMessage.getNickname();
                ConnectionMessage connectionMessage = (ConnectionMessage) firstMessage;

                /**
                 * Check of the nickname-univocity
                 */
                //check playing-clients
                if (!disconnectedPlayers.contains(nickname)) {
                    usedNicknames.clear();
                    for (int i : ServerController.getInstance().getCurrentGames().keySet()) {
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
                    }
                } else {
                    usedNicknames.remove(nickname);
                    //si è riconnesso
                    //GESTIRE
                }

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


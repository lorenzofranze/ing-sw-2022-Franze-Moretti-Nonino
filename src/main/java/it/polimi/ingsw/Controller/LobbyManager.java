package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Messages.ClientMessage;
import it.polimi.ingsw.Model.Messages.ConnectionMessage;
import it.polimi.ingsw.Model.Messages.JsonConverter;
import it.polimi.ingsw.Model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LobbyManager {
    private Map<GameMode, Lobby> waitingLobbies;
    private ServerController serverController;
    private ServerSocket lobbyServerSocket;
    private int lobbyPortNumber;

    public LobbyManager(int lobbyPortNumber){
        this.waitingLobbies=new HashMap<>();
        this.lobbyPortNumber=lobbyPortNumber;
        this.serverController=ServerController.getIntance();
        try {
            lobbyServerSocket = new ServerSocket(lobbyPortNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /** the server listen for clients. One client aat time is listened. The client connects with a
     * connection-message telling its nickname and its preferred game-mode.
     * A buffer in reader is created.
     * It is read the first message and it is checked the validity of the nickname(no other connected-players
     * can have the same nickname)
     * @throws IOException
     */

    /**
     * ATTENTION: POSSIBLE ATTACK FROM A MALITIOUS CLIENT: never sends its nickname: i have to put a timeout
     * otherwise no other players can connect and start a new game
     */
    public void welcomeNewPlayers() throws IOException {
        JsonConverter jsonConverter=new JsonConverter();
        ArrayList<String> usedNicknames= new ArrayList<>();
        ClientMessage firstMessage;
        while(true){
            Socket clientSocket = null;
            try {
                clientSocket = lobbyServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            firstMessage=jsonConverter.fromJsonToMessage(in.readLine());
            if(firstMessage.getHeader().getMessageType()=="ConnectionMessage"){
                String nickname=firstMessage.getHeader().getNickname();
                ConnectionMessage connectionMessage= (ConnectionMessage) firstMessage;

                /**
                 * Check of the nickname-univocity
                 */
                //check playing-clients
                usedNicknames.clear();
                for(int i=0; i<serverController.getGameControllers().size(); i++){
                    for( Player p: serverController.getGameControllers().get(i).getGame().getPlayers() ){
                        usedNicknames.add(p.getNickname());
                    }
                }
                //check waiting-clients
                for(GameMode gameMode: GameMode.values()){
                    for(Lobby lobby: waitingLobbies.values() ){
                        usedNicknames.addAll(lobby.getUsersNicknames());
                    }
                }

                if(!usedNicknames.contains(nickname)){
                    GameMode gameMode=connectionMessage.getGameMode();
                    addNickname(nickname, gameMode, clientSocket);
                }
            }
        }
    }

    public void addNickname(String nickname, GameMode mode, Socket clientSocket){
        if(waitingLobbies.containsKey(mode)){
            waitingLobbies.get(mode).addUsersReadyToPlay(nickname,clientSocket);

            if(waitingLobbies.get(mode).getUsersReadyToPlay().size()==mode.getNumPlayers()){
                this.serverController.addGameController(mode, waitingLobbies.get(mode));
                waitingLobbies.remove(mode);
            }
        }
        else{
            Lobby newLobby= new Lobby();
            newLobby.addUsersReadyToPlay(nickname,clientSocket);
            waitingLobbies.put(mode,newLobby);
        }
    }

}

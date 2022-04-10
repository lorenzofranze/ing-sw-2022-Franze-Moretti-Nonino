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
import java.util.Map;

public class LobbyManager {
    private Map<GameMode, Lobby> waitingLobbies;
    private ServerController serverController;
    private ServerSocket lobbyServerSocket;
    private int lobbyPortNumber;

    public LobbyManager(int lobbyPortNumber){
        this.lobbyPortNumber=lobbyPortNumber;
        this.serverController=ServerController.getIntance();
        try {
            lobbyServerSocket = new ServerSocket(lobbyPortNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void welcomeNewPlayers() throws IOException {
        JsonConverter jsonConverter=new JsonConverter();
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
                GameMode gameMode=connectionMessage.getGameMode();
                addNickname(nickname, gameMode);

            }
        }
    }

    public void addNickname(String nickname, GameMode mode){
        if(waitingLobbies.containsKey(mode)){
            waitingLobbies.get(mode).addUsersReadyToPlay(nickname);

            if(waitingLobbies.get(mode).getUsersReadyToPlay().size()==mode.getNumPlayers()){
                this.serverController.addGameController(mode, waitingLobbies.get(mode));
                waitingLobbies.remove(mode);
            }
        }
        else{
            Lobby newLobby= new Lobby();
            newLobby.addUsersReadyToPlay(nickname);
            waitingLobbies.put(mode,newLobby);
        }
    }



}

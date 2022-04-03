package it.polimi.ingsw.Controller;

import java.util.Map;

public class LobbyManager {
    private Map<GameMode, Lobby> waitingLobbies;
    private ServerController serverController;

    public LobbyManager(){
        this.serverController=ServerController.getIntance();
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

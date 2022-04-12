package it.polimi.ingsw.Controller;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lobby {
    // string è nickname
    private Map<String, Socket> usersReadyToPlay;

    public GameMode getGameMode() {
        return gameMode;
    }

    private GameMode gameMode;

    public Map<String, Socket> getUsersReadyToPlay() {
        return usersReadyToPlay;
    }

    public Lobby(GameMode gameMode){
        this.gameMode = gameMode;
    }

    public List<String> getUsersNicknames() {
        ArrayList<String> nicknames= new ArrayList<>();
        nicknames.addAll(usersReadyToPlay.keySet());
        return nicknames;
    }

    public void addUsersReadyToPlay(String nickname, Socket clientSocket){
        usersReadyToPlay.put(nickname,clientSocket);
    }
}

package it.polimi.ingsw.Controller;

import java.util.List;

public class Lobby {
    private List<String> usersReadyToPlay;

    public List<String> getUsersReadyToPlay() {
        return usersReadyToPlay;
    }

    public void addUsersReadyToPlay(String userReadyToPlay){
        usersReadyToPlay.add(userReadyToPlay);
    }
}

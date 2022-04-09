package it.polimi.ingsw.Model.Messages;

import it.polimi.ingsw.Controller.GameMode;

public class ConnectionMessage implements Message{
    private String nickname;
    private GameMode gameMode;


    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getNickname() {
        return nickname;
    }
}

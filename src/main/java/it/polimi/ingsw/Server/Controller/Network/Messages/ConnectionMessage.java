package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Controller.GameMode;

public class ConnectionMessage extends ClientMessage {

    private String nickname;
    private GameMode gameMode;

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getNickname() {
        return this.nickname;
    }
}

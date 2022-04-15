package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Controller.GameMode;

public class ConnectionMessage extends ClientMessage  {

    private GameMode gameMode;

    public ConnectionMessage(){
        this.setMessageType("ConnectionMessage");
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getNickname() {
        return this.getNickname();
    }
}

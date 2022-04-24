package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Controller.GameMode;

public class ConnectionMessage extends ClientMessage {

    private final String nickname;
    private final GameMode gameMode;

    public ConnectionMessage(String nickname, GameMode gameMode){
        this.nickname = nickname;
        this.gameMode=gameMode;
        this.setMessageType(TypeOfMessage.Connection);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getNickname() {
        return this.nickname;
    }

    @Override
    public String toString() {
        return "ConnectionMessage{" +
                "nickname='" + nickname + '\'' +
                ", gameMode=" + gameMode +
                '}';
    }
}

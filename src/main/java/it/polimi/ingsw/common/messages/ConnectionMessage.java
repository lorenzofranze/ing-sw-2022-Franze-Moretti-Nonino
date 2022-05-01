package it.polimi.ingsw.common.messages;

import it.polimi.ingsw.server.controller.logic.GameMode;

public class ConnectionMessage extends Message {

    private final String nickname;
    private final GameMode gameMode;

    public ConnectionMessage(TypeOfMessage typeOfMessage, String nickname, GameMode gameMode){
        super(typeOfMessage);
        this.nickname = nickname;
        this.gameMode=gameMode;
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

package it.polimi.ingsw.common.messages;

import it.polimi.ingsw.server.controller.logic.GameMode;

/**
 * Messages for connections
 */
public class ConnectionMessage extends Message {

    private final String nickname;
    private final GameMode gameMode;

    public ConnectionMessage(String nickname, GameMode gameMode){
        super(TypeOfMessage.Connection);
        this.nickname = nickname;
        this.gameMode=gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
    public String getNickname() {
        return this.nickname;
    }
}

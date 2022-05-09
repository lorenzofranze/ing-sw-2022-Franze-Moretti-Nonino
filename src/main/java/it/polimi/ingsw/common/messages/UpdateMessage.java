package it.polimi.ingsw.common.messages;

import it.polimi.ingsw.common.gamePojo.GameStatePojo;

public class UpdateMessage extends Message {

    private GameStatePojo gameStatePojo;

    public UpdateMessage(GameStatePojo gameStatePojo){
        super(TypeOfMessage.Update);
        this.gameStatePojo = gameStatePojo;
    }

    public GameStatePojo getGameState() {
        return gameStatePojo;
    }
}

package it.polimi.ingsw.common.messages;

import it.polimi.ingsw.common.gamePojo.GameState;
import it.polimi.ingsw.server.controller.characters.CharacterEffect;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.Character;

import java.util.Map;

public class UpdateMessage extends Message {

    private GameState gameState;

    public UpdateMessage(TypeOfMessage typeOfMessage, GameState gameState){
        super(typeOfMessage);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

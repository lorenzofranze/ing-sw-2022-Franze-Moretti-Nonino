package it.polimi.ingsw.server.Controller.Messages;

import it.polimi.ingsw.server.Model.AssistantCard;

public class AssistantCardMessage extends ClientMessage {
    private AssistantCard card;
    private GameState gameState;

    public AssistantCard getCard() {
        return card;
    }

    public GameState getGameState() {
        return gameState;
    }
}

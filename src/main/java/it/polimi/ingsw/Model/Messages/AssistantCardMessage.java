package it.polimi.ingsw.Model.Messages;

import it.polimi.ingsw.Model.AssistantCard;

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

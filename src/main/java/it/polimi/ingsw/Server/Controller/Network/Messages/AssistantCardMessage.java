package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Model.AssistantCard;

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

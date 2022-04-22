package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Model.AssistantCard;

public class AssistantCardMessage extends ClientMessage {
    private Integer card;

    public Integer getCard() {
        return card;
    }

}

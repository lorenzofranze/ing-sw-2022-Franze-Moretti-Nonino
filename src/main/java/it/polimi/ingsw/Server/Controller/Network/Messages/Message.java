package it.polimi.ingsw.Server.Controller.Network.Messages;

import java.awt.*;

public abstract class Message {
    private TypeOfMessage messageType;

    public TypeOfMessage getMessageType() {
        return messageType;
    }

    public void setMessageType(TypeOfMessage messageType) {
        this.messageType = messageType;
    }
}

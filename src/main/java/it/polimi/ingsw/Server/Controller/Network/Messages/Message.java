package it.polimi.ingsw.Server.Controller.Network.Messages;

import java.awt.*;

public abstract class Message {
    private TypeOfMessage typeOfMessage;

    public TypeOfMessage getMessageType() {
        return typeOfMessage;
    }

    public void setMessageType(TypeOfMessage messageType) {
        this.typeOfMessage = messageType;
    }
}

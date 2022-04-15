package it.polimi.ingsw.Server.Controller.Network.Messages;

public abstract class Message {
    private String messageType;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}

package it.polimi.ingsw.server.Controller.Network.Messages;

public class ClientMessageHeader {
    private String nickname;
    private String messageType;

    public String getMessageType() {
        return messageType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}

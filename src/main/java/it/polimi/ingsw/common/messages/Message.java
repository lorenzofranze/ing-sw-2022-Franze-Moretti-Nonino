package it.polimi.ingsw.common.messages;

public class Message {
    private TypeOfMessage typeOfMessage;

    public Message(TypeOfMessage typeOfMessage) {
        this.typeOfMessage = typeOfMessage;
    }

    public TypeOfMessage getMessageType() {
        return typeOfMessage;
    }
}

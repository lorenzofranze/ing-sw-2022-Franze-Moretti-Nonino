package it.polimi.ingsw.common.messages;

public class StringMessage extends Message{

    private String stringMessage;

    public StringMessage(TypeOfMessage typeOfMessage, String stringToSend) {
        super(typeOfMessage);
        this.setStringMessage(stringToSend);
    }

    public String getStringMessage() {
        return stringMessage;
    }

    public void setStringMessage(String stringMessage) {
        this.stringMessage = stringMessage;
    }
}

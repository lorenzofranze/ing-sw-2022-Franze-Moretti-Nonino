package it.polimi.ingsw.common.messages;

public class StringMessage extends Message{

    private String stringMessage;

    public StringMessage(String stringToSend) {
        super(TypeOfMessage.String);
        this.setStringMessage(stringToSend);
    }

    public String getStringMessage() {
        return stringMessage;
    }

    public void setStringMessage(String stringMessage) {
        this.stringMessage = stringMessage;
    }
}

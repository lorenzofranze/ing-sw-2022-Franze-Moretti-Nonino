package it.polimi.ingsw.Server.Controller.Network.Messages;

public class StringMessage extends ServerMessage{
    private String stringMessage;



    public StringMessage(String stringMessage) {
        super(TypeOfMessage.String);
        this.stringMessage=stringMessage;
    }
}

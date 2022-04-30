package it.polimi.ingsw.Server.Controller.Network.Messages;

public class StringMessage extends Message{
    private String stringMessage;

    public StringMessage(String stringMessage){
        this.stringMessage=stringMessage;
    }
}

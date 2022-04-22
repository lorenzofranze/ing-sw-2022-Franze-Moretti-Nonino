package it.polimi.ingsw.Server.Controller.Network.Messages;

public class StringMessage extends Message{
    private String nickName;
    private String stringMessage;

    public StringMessage(String nickName, String stringMessage){
        this.nickName=nickName;
        this.stringMessage=stringMessage;
    }
}

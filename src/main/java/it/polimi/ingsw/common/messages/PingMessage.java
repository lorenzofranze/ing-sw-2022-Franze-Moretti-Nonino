package it.polimi.ingsw.common.messages;

public class PingMessage extends Message{
    public PingMessage(){
        super(TypeOfMessage.Ping);
    }
}

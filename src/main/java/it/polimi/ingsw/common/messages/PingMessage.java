package it.polimi.ingsw.common.messages;

/**
 * Messages for ping
 */
public class PingMessage extends Message{
    public PingMessage(){
        super(TypeOfMessage.Ping);
    }
}

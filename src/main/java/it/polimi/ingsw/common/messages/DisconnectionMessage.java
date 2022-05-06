package it.polimi.ingsw.common.messages;

public class DisconnectionMessage extends Message{

    private String s;

    public DisconnectionMessage(){
        super(TypeOfMessage.Async);
    }
    public DisconnectionMessage(String s){
        super(TypeOfMessage.Async);
        this.s=s;
    }

}

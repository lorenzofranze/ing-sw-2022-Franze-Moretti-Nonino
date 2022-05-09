package it.polimi.ingsw.common.messages;

public class AckMessage extends Message{
    private TypeOfAck typeOfAck;
    private String description;

    public AckMessage(TypeOfAck typeOfAckMessage, String description){
        super(TypeOfMessage.Ack);
        this.typeOfAck = typeOfAckMessage;
        this.description = description;
    }

    public AckMessage(TypeOfAck typeOfAckMessage){
        super(TypeOfMessage.Ack);
        this.typeOfAck = typeOfAckMessage;
        this.description = "";
    }

    public String getDescription() {
        return description;
    }
    public TypeOfAck getTypeOfAck() {
        return typeOfAck;
    }
}

package it.polimi.ingsw.common.messages;

public class GameMessage extends Message{
    private int value;

    public GameMessage(TypeOfMessage typeOfMessage, int value){
        super(typeOfMessage);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

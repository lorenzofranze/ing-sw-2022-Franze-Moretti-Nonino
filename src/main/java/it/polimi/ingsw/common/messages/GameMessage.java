package it.polimi.ingsw.common.messages;

public class GameMessage extends Message{

    private TypeOfMove typeOfMove;
    private int value;

    public GameMessage(TypeOfMove typeOfMove, int value){
        super(TypeOfMessage.Game);
        this.typeOfMove = typeOfMove;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public TypeOfMove getTypeOfMove() {
        return typeOfMove;
    }
}

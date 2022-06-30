package it.polimi.ingsw.common.messages;

/**
 * Messages for move-choices in the game
 */
public class GameMessage extends Message{

    private TypeOfMove typeOfMove;
    private Integer value;

    public GameMessage(TypeOfMove typeOfMove, Integer value){
        super(TypeOfMessage.Game);
        this.typeOfMove = typeOfMove;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public TypeOfMove getTypeOfMove() {
        return typeOfMove;
    }
}

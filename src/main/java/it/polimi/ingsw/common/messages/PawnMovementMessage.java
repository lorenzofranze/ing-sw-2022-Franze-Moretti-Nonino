package it.polimi.ingsw.common.messages;

public class PawnMovementMessage extends GameMessage {

    private int where;

    public PawnMovementMessage(int pawnColour, int where){
        super(TypeOfMove.PawnMovement, pawnColour);
        this.where=where;
    }

    public int getColour(){return super.getValue();}
    public int getWhere(){
        return where;
    }
}

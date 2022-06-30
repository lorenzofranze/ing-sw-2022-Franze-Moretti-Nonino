package it.polimi.ingsw.common.messages;

/**
 * Messages for move-choices in the game - specific for pawn movements in which the choice isn't only about the colour
 * but also about the position (two kind of choices)
 */
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

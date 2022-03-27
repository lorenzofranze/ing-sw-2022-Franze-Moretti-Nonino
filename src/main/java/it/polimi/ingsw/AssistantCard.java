package it.polimi.ingsw;

public class AssistantCard {
    private final int turnOrder;
    private final int movementsMotherNature;

    /** initializes AssistantCard's attributes **/
    public AssistantCard(int turnOrder, int movementsMotherNature){
        this.turnOrder = turnOrder;
        this.movementsMotherNature = movementsMotherNature;
    }
    /** return the turn order on the card **/
    public int getTurnOrder(){
        return this.turnOrder;
    }
    /** return the max number of movements Mother nature can do **/
    public int getMovementsMotherNature(){
        return this.movementsMotherNature;
    }

}

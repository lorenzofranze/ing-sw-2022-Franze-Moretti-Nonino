package it.polimi.ingsw.server.model;

import java.util.Objects;

public class AssistantCard implements java.io.Serializable{
    private final int turnOrder;
    private final int movementsMotherNature;

    /** initializes AssistantCard's attributes */
    public AssistantCard(int turnOrder, int movementsMotherNature){
        this.turnOrder = turnOrder;
        this.movementsMotherNature = movementsMotherNature;
    }

    /** return the turn order on the card */
    public int getTurnOrder(){
        return this.turnOrder;
    }
    /** return the max number of movements Mother nature can do */
    public int getMovementsMotherNature(){
        return this.movementsMotherNature;
    }


    // TEST CLASSES :
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantCard that = (AssistantCard) o;
        return turnOrder == that.turnOrder && movementsMotherNature == that.movementsMotherNature;
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnOrder, movementsMotherNature);
    }

    @Override
    public String toString() {
        return "AssistantCard{" +
                "turnOrder=" + turnOrder +
                ", movementsMotherNature=" + movementsMotherNature +
                '}';
    }
}

package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.AssistantCardPojo;

import java.util.Objects;

/**
 * This class cointains the informations about the AssistantCards: the turnOrder and
 * the movementsMotherNature of the card
 */
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

    /**returns an AssistantCardPojo representing this AssistantCard*/
    public AssistantCardPojo toPojo(){
        AssistantCardPojo pojoAssistantCard = new AssistantCardPojo();
        pojoAssistantCard.setTurnOrder(this.getTurnOrder());
        pojoAssistantCard.setMovementsMotherNature(this.getMovementsMotherNature());
        return pojoAssistantCard;
    }
}

package it.polimi.ingsw.common.gamePojo;

import it.polimi.ingsw.server.model.AssistantCard;

import java.util.ArrayList;
import java.util.Collections;

public class AssistantCardPojo {

    private int turnOrder;
    private int movementsMotherNature;

    public int getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(int turnOrder) {
        this.turnOrder = turnOrder;
    }

    public int getMovementsMotherNature() {
        return movementsMotherNature;
    }

    public void setMovementsMotherNature(int movementsMotherNature) {
        this.movementsMotherNature = movementsMotherNature;
    }

    @Override
    public String toString(){
        String ris = "\t\tmovementsMotherNature: " + movementsMotherNature;
        ris = ris + "\t\tturnOrder: " + turnOrder;
        return ris;
    }

    public AssistantCard getAssistantCard(){
        AssistantCard assistantCard = new AssistantCard(turnOrder, movementsMotherNature);
        return assistantCard;
    }
}

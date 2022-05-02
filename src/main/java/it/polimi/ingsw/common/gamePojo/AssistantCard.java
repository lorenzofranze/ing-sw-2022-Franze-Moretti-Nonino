package it.polimi.ingsw.common.gamePojo;

public class AssistantCard {

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

    public AssistantCard(it.polimi.ingsw.server.model.AssistantCard assistantCard){
        this.turnOrder = assistantCard.getTurnOrder();
        this.movementsMotherNature = assistantCard.getMovementsMotherNature();
    }
}

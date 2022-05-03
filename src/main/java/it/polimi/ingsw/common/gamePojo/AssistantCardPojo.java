package it.polimi.ingsw.common.gamePojo;

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

    public AssistantCardPojo(it.polimi.ingsw.server.model.AssistantCard assistantCard){
        this.turnOrder = assistantCard.getTurnOrder();
        this.movementsMotherNature = assistantCard.getMovementsMotherNature();
    }
}

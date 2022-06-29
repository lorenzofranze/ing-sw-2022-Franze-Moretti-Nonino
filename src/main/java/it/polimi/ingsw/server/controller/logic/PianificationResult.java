package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.server.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Useful to give to the Action phase the informations about the maximumMovements of mother nature and turnOrder
 * calculated according to the assistant card chosen by the players in the previous PianificationPhase
 */

public class PianificationResult {
    private HashMap<Player, Integer> maximumMovements;
    private List<Player> turnOrder;

    private boolean finishedAssistantCard;
    private boolean finishedStudentBag;

    public PianificationResult(){
        maximumMovements = new HashMap<Player, Integer>();
        turnOrder = new ArrayList<Player>();
    }

    public HashMap<Player, Integer> getMaximumMovements() {
        return maximumMovements;
    }

    public void setMaximumMovements(HashMap<Player, Integer> maximumMovements) {
        this.maximumMovements = maximumMovements;
    }

    public List<Player> getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(List<Player> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public boolean isFinishedAssistantCard() {
        return finishedAssistantCard;
    }

    public void setFinishedAssistantCard(boolean finishedAssistantCard) {
        this.finishedAssistantCard = finishedAssistantCard;
    }

    public boolean isFinishedStudentBag() {
        return finishedStudentBag;
    }

    public void setFinishedStudentBag(boolean finishedStudentBag) {
        this.finishedStudentBag = finishedStudentBag;
    }

    @Override
    public boolean equals(Object o){
        PianificationResult o1;
        if (o == null){
            return false;
        }
        if (o instanceof SetUpPhase){
            o1 = (PianificationResult) o;
        }else{
            return false;
        }

        if (!this.getMaximumMovements().equals(o1.maximumMovements)){
            return false;
        }
        if (!this.getTurnOrder().equals(o1.turnOrder)){
            return false;
        }
        if (this.finishedAssistantCard != o1.finishedAssistantCard){
            return false;
        }
        if (this.finishedStudentBag != o1.finishedStudentBag){
            return false;
        }
        return true;
    }
}

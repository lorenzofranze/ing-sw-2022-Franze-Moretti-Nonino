package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PianificationResult extends PhaseResult{
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


    
}

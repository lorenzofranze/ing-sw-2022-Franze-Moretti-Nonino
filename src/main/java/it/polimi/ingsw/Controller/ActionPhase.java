package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.ColourPawn;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionPhase implements GamePhase {

    private GameController gameController;
    private HashMap<Player, Integer> maximumMovements;
    private List<Player> turnOrder;

    public ActionPhase(GameController gc) {
        this.gameController = gc;

        this.maximumMovements = new HashMap<Player, Integer>();
        List<Player> list = gc.getGame().getPlayers();
        for(Player p : list){
            maximumMovements.put(p, 0);
        }

        turnOrder = new ArrayList<>(list.size());
    }

    public void setMaximumMovements(HashMap<Player, Integer> maximumMovements) {
        this.maximumMovements = maximumMovements;
    }

    public void setTurnOrder(List<Player> turnOrder) {
        this.turnOrder = turnOrder;
    }

    @Override
    public void handle() {

    }



}

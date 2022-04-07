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

    private void moveStudents(){
        boolean valid=true;
        int indexColour;
        int where;   // -1 refer for diningRoom, index of island for island

        for(int i=0; i<3; i++){
            // to user: choose i+1 movement of 3
            do{
                // to user: chhose one color pawn
                indexColour = messageHandel.getValue();
                if(indexColour<=-1 || indexColour >=5){
                    valid=false;
                    // to user: index not valid
                }
                if(valid){
                    if(! (gameController.getGame().getCurrentPlayer().getSchoolBoard()
                            .getEntrance().get(ColourPawn.values()[indexColour]) >=1)){
                        valid = false;
                        //to user: change color pawn to move
                    }
                }

                // to user: choose position

                if(valid){
                    where = messageHandel.getValues();
                    if(where!= -1 && (where <0 || where > gameController.getGame().getIslands().size()-1 ){
                        valid = false;
                        //to user: position not valid
                    }
                }
            }while(!valid);

            // to user: ok
            this.moveSingleStudent(ColourPawn.values()[indexColour], where);
            valid=true;

        }
    }



}

package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;

import java.util.*;

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

    @Override
    public void handle() {


    }


    /** if where==-1 moves student from the schoolboard entrance to the diningroom
     * else moves student from the schoolboard entrance to the island of index=where
     * @param colour
     * @param where
     */
    public void moveSingleStudent(ColourPawn colour, Integer where){
        PawnsMap student= new PawnsMap();
        student.add(colour);

        if (where == -1) {
            gameController.getCurrentPlayer().getSchoolBoard().fromEntranceToDiningRoom(student, gameController.getGame());
        } else {
            gameController.getCurrentPlayer().getSchoolBoard().removeFromDiningRoom(student);
            gameController.getGame().getIslands().get(where).addStudents(student);
        }

    }


    public void setCurrPlayer(Player currPlayer) {
        this.gameController.setCurrentPlayer(currPlayer);
    }



    public void setMaximumMovements(HashMap<Player, Integer> maximumMovements) {
        this.maximumMovements = maximumMovements;
    }

    public void setTurnOrder(List<Player> turnOrder) {
        this.turnOrder = turnOrder;
    }

    private void computePlayerPianification(){
        Player firstInPianification = turnOrder.get(0);
        PianificationPhase p = (PianificationPhase) this.gameController.getPianificationPhase();
        p.setFirstPlayer(firstInPianification);
    }

    private void moveStudents(){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        boolean valid=true;
        int indexColour;
        int where=0 ;   // -1 refer for diningRoom, index of island for island

        for(int i=0; i<3; i++){
            // to user: choose i+1 movement of 3
            do{
                // to user: chhose one color pawn
                indexColour = messageHandler.getValue();
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
                    where = messageHandler.getValue();
                    if(where!= -1 && (where <0 || where > gameController.getGame().getIslands().size()-1 )) {
                        valid = false;
                        //to user: position not valid
                    }
                }
            }while(!valid);

            // to user: ok
            this.moveSingleStudent(ColourPawn.values()[indexColour], where );
            valid=true;

        }
    }

    private void moveMotherNature(Player currentPlayer){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int played;

        do{
            played = messageHandler.getValue(currentPlayer);
        }
        while(played < 1 || played > maximumMovements.get(currentPlayer));

        List<Island> islandList = this.gameController.getGame().getIslands();

        boolean flag = false;
        for(int i = 0; i < islandList.size() && flag == false; i++){
            if (islandList.get(i).getHasMotherNature() == true){
                flag = true;
                islandList.get(i).setHasMotherNature(false);
                islandList.get(i+played).setHasMotherNature(true);

                calcultateInfluence(islandList.get(i+played));
            }
        }
    }


    //è da fare??
    public void calcultateInfluence(Island island){

    }

    /**places a tower on a island or swap the color, then, if necessary, calls verifyUnion()
     * @param colour
     * @param island
     */
    public void placeTower(ColourTower colour, Island island){
        if(island.getTowerCount()==0){
            island.addTower(1);
            island.setTowerColor(colour);
            verifyUnion();
        }
        if(!island.getTowerColour().equals(colour)){
            island.setTowerColor(colour);
            verifyUnion();
        }
    }

    private void verifyUnion() {
        List<Island> islandList = this.gameController.getGame().getIslands();
        List<Integer> currColour;
        HashMap<ColourTower, List<Integer>> colourMap = new HashMap<ColourTower, List<Integer>>();

        for(ColourTower c : ColourTower.values()){
            currColour = new ArrayList<>();
            colourMap.put(c, currColour);
        }
        for(int i = 0; i < islandList.size(); i++){
            if (islandList.get(i).getTowerCount() > 0){
                colourMap.get(islandList.get(i).getTowerColour()).add(i);
            }
        }

        /*at this point colourMap is a map where the key is a ColourTower and the value contained is a List of Integer
        corrisponding at the index of the isalnds where the tower has that colour*/

        boolean flag = false; //true when islands of the same colour are adjacent
        Set<Integer> toUnify = new HashSet<>();//set of the islands index to unify

        for(ColourTower c : ColourTower.values()) {
            currColour = colourMap.get(c);

            for(Integer i : currColour){
                for(Integer j : currColour){
                    if (j.equals((i+1) % islandList.size())){
                        toUnify.add(i);
                        toUnify.add(j);
                        flag = true;
                    }
                }
            }
        }


        List<Island> ris = new ArrayList<>(); //list of the islands to unify
        if (flag == true){
            for(Integer i : toUnify){
                ris.add(islandList.get(i));
            }
        }

        this.gameController.getGame().unifyIslands(ris);
    }

}

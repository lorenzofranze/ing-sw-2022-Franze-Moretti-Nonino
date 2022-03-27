package it.polimi.ingsw;

import java.util.*;

public class SchoolBoard {
    private PawnsMap professorsTable;
    private PawnsMap diningRoom;
    private PawnsMap entrance;
    private int spareTowers;

    /** initializes values and set numberOfTowers to 8,
     * if there are 3 players
     * the value will be decremented to 6
     */
    public SchoolBoard(){

        this.professorsTable = new PawnsMap();
        this.diningRoom = new PawnsMap();
        this.entrance = new PawnsMap();
        Controller controller = Controller.getIntance();

        if (controller.getGame().getPlayers().size() == 2){
            this.spareTowers = 8;
        }
        else{this.spareTowers = 6;}
    }

    /** add a tower to the schoolboard**/
    public void addTower(){
        this.spareTowers ++;
    }

    /** remove a tower from the schoolboard*/
    public void removeTower() {
        this.spareTowers --;
    }

    public PawnsMap getDiningRoom() {
        return diningRoom;
    }

    public PawnsMap getEntrance() {
        return entrance;
    }

    public PawnsMap getProfessorsTable() {
        return professorsTable;
    }
}

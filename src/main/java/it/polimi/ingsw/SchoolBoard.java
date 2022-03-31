package it.polimi.ingsw;

import java.util.*;

/* ho tolto i riferimenti a controller perchè se servono 6
* torri nella setup phase verrà chimato il metodo removeTowers
* per toglierne 2
 */

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
        this.spareTowers = 8;
    }

    /** add a tower to the schoolboard**/
    public void addTower(){
        this.spareTowers ++;
    }

    /** remove a tower from the schoolboard*/
    public void removeTower() {
        this.spareTowers --;
    }

    /** return the map of students in dining room */
    public PawnsMap getDiningRoom() {
        return diningRoom;
    }

    /** return the the map of students in dining room */
    public PawnsMap getEntrance() {
        return entrance;
    }

    /** return the map of prefessors */
    public PawnsMap getProfessorsTable() {
        return professorsTable;
    }

    /** remove the professor*/
    public void removeProfessor(ColourPawn colourPawn){
        professorsTable.removePawns(colourPawn,1);
        return;
    }
    /**add the professor*/
    public void addProfessor(ColourPawn colourPawn){
        professorsTable.addPawns(colourPawn,1);
        return;
    }

}

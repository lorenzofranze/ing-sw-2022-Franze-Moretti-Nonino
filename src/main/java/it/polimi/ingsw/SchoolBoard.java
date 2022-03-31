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

    /** @return a map copy of students in dining room, to add students in
     * dining room use the special method: addToDiningRoom */
    public PawnsMap getDiningRoom() {
        return this.diningRoom.clone();
    }

    /** return the the map of students in dining room */
    public PawnsMap getEntrance() {
        return entrance;
    }

    /** return the map of prefessors */
    public PawnsMap getProfessorsTable() {
        return professorsTable;
    }

    /** return the number of coins to add after the movement
     * and add the students in input to dining room */
    public int addToDiningRoom(PawnsMap pawns){
        ColourPawn pawnsList[] = ColourPawn.values();
        int num;
        int coinsToAdd=0;
        for(ColourPawn p : pawnsList){
            num = pawns.getPawns(p);
            if(num>0){
                for(int i=0; i<num; i++) {
                    this.diningRoom.addPawn(p);
                    if (this.diningRoom.getPawns(p) == 3 || this.diningRoom.getPawns(p) == 6 || this.diningRoom.getPawns(p) == 3)
                        coinsToAdd ++;
                }
            }
        }
        return coinsToAdd;
    }

    /** receives in input a cloud and inserts all the students in entrance */
    public void insertCloud(Cloud cloud){
        ColourPawn pawnsList[] = ColourPawn.values();
        int num;
        for(ColourPawn p : pawnsList){
            num = cloud.getStudents().getPawns(p);
            if( num > 0){
                this.entrance.addPawns(p, num);
            }
        }
    }

    /** move students in input to the dining room and remove from
     * the entrance, return the number of coins to add after the movement */
    public int moveToDiningRoom(PawnsMap toMove){
        ColourPawn pawnsList[] = ColourPawn.values();
        int num;
        int coinsToAdd=0;
        coinsToAdd = this.addToDiningRoom(toMove);
        for(ColourPawn p : pawnsList){
            num = toMove.getPawns(p);
            if( num > 0){
                this.entrance.removePawns(p, num);
            }
        }
        return coinsToAdd;
    }

}

package it.polimi.ingsw;

import java.util.*;

/* ho tolto i riferimenti a controller perchè se servono 6
* torri nella setup phase verrà chimato il metodo removeTowers
* per toglierne 2
 */

public class SchoolBoard {
    private PawnsMap professors;
    private PawnsMap diningRoom;
    private PawnsMap entrance;
    private int spareTowers;

    /** initializes values and set numberOfTowers to 8,
     * if there are 3 players
     * the value will be decremented to 6
     */
    public SchoolBoard(){

        this.professors = new PawnsMap();
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
        if (this.spareTowers > 0) this.spareTowers --;
    }

    /** @return the clone of the DiningRoom map */
    public PawnsMap getDiningRoom() {
        return this.diningRoom.clone();
    }

    /** @return the clone of the Entrance map */
    public PawnsMap getEntrance() {
        return this.entrance.clone();
    }

    /** @return the clone of the PrefessorsTable map */
    public PawnsMap getProfessors() {
        return this.professors.clone();
    }

    /**@Return the number of towers left in the schoolboard*/
    public int getSpareTowers(){
        return this.spareTowers;
    }

    /** remove the professor*/
    public void removeProfessor(ColourPawn colourPawn){
        professors.remove(colourPawn);
        return;
    }
    /**add the professor*/
    public void addProfessor(ColourPawn colourPawn){
        professors.add(colourPawn);
        return;
    }


    /**@return the number of coins to add after the movement
     * and add the students in input to dining room */
    public int addToDiningRoom(PawnsMap pawns){
        ColourPawn pawnsList[] = ColourPawn.values();
        int num;
        int coinsToAdd=0;
        for(ColourPawn p : pawnsList){
            num = pawns.get(p);
            if(num>0){
                for(int i=0; i<num; i++) {
                    this.diningRoom.add(p);
                    if (this.diningRoom.get(p) % 3 == 0)
                        coinsToAdd ++;
                }
            }
        }
        return coinsToAdd;
    }

    /** add the students in input to dining room */
    public void addToEntrance(PawnsMap pawns){
        ColourPawn pawnsList[] = ColourPawn.values();
        int num;
        for(ColourPawn p : pawnsList){
            num = pawns.get(p);
            if(num>0){
                for(int i=0; i<num; i++) {
                    this.entrance.add(p);
                }
            }
        }
    }


    /** receives in input a cloud and inserts all the students in entrance */
    public void insertCloud(Cloud cloud){
        this.entrance.add(cloud.clearCloud());
    }

    /** move students in input to the dining room and remove from
     * the entrance, return the number of coins to add after the movement */
    public int fromEntranceToDiningRoom(PawnsMap toMove){
        ColourPawn pawnsList[] = ColourPawn.values();
        int coinsToAdd=0;
        coinsToAdd = this.addToDiningRoom(toMove);
        this.entrance.remove(toMove);
        return coinsToAdd;
    }

}

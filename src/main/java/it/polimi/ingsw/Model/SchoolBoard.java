package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.ColourPawn;
import it.polimi.ingsw.Model.PawnsMap;

import java.util.Objects;

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
         this.spareTowers --;
    }

    public PawnsMap getDiningRoom() {
        return this.diningRoom.clone();
    }

    /** this method will be used for witch character card
     * don't calls verify professors because isn't necessary
     * @param pawns
     */
    public void removeFromDiningRoom(PawnsMap pawns){
        this.diningRoom.remove(pawns);

    }

    public PawnsMap getEntrance() {
        return this.entrance;
    }

    public PawnsMap getProfessors() {
        return this.professors;
    }

    /**@Return the number of towers left in the schoolboard*/
    public int getSpareTowers(){
        return this.spareTowers;
    }


    /** adds the students in input to dining room
     * @return the number of coins to add after the movement*/
    public int addToDiningRoom(PawnsMap pawns, Game game){
        ColourPawn pawnsList[] = ColourPawn.values();
        int num;
        int coinsToAdd=0;
        for(ColourPawn colour : pawnsList){
            num = pawns.get(colour);
            if(num>0){
                for(int i=0; i<num; i++) {
                    this.diningRoom.add(colour);
                    if (this.diningRoom.get(colour) % 3 == 0)
                        coinsToAdd ++;
                }
                this.verifyProfessorInfluence(game, colour);
            }
        }

        return coinsToAdd;
    }


    /** receives in input a cloud and inserts all the students in entrance */
    public void insertCloud(Cloud cloud){
        this.entrance.add(cloud.clearCloud());
    }

    /** move students in input to the dining room and remove from
     * the entrance, return the number of coins to add after the movement */
    public int fromEntranceToDiningRoom(PawnsMap toMove, Game game){
        int coinsToAdd=0;
        coinsToAdd = this.addToDiningRoom(toMove, game);
        this.entrance.remove(toMove);
        return coinsToAdd;
    }

    /** verifies and updates all the school board:
     * move the professors from professorLeft map or other player's
     * schoolboard to the school board with more influence
     *
     */

    private void verifyProfessorInfluence(Game game, ColourPawn colour){
        if(game.getProfessorsLeft().get(colour) == 1){
            game.getProfessorsLeft().remove(colour);
            this.professors.add(colour);
            return;
        }

        for(Player p : game.getPlayers()){
            if(! (p.getSchoolBoard() == this)){
                if(!(p.getSchoolBoard().professors.get(colour)==0) &&  this.diningRoom.get(colour) > p.getSchoolBoard().diningRoom.get(colour)){
                    this.professors.add(colour);
                    p.getSchoolBoard().professors.remove(colour);
                }
            }
        }

    }

}


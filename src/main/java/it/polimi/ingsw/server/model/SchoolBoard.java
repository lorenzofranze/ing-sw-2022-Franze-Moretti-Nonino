package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.ColourPawn;

/**
 * This class defines methods to add, remove and count towers, professors and students from the schoolboard.
 * Moreover it verifies the professors influence to choose what player own the professsor of a specific colour
 * according to the number of students present in the diningroom
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

    public void addTower(int num){
        this.spareTowers +=num;
    }

    /** remove a tower from the schoolboard*/
    public void removeTower() {
         this.spareTowers --;
    }

    /** remove num towers from the schoolboard*/
    public void removeTower(int num) {
        this.spareTowers -= num;
    }

    public PawnsMap getDiningRoom() {
        return this.diningRoom;
    }

    /** this method will be used for witch character card
     * don't calls verify professors because isn't necessary
     * param: PawnsMap
     */
    public void removeFromDiningRoom(PawnsMap pawns){this.diningRoom.remove(pawns);}

    /** swap 2 pawns between diningRoom and entrance, update professors */
    public void swap(ColourPawn wasEntrance, ColourPawn wasDiningRoom, Game game){
        this.entrance.remove(wasEntrance);
        this.diningRoom.remove(wasDiningRoom);
        this.entrance.add(wasDiningRoom);
        PawnsMap map = new PawnsMap();
        map.add(wasEntrance);
        this.addToDiningRoom(map, game);
        game.reassignProfessors();}

    public PawnsMap getEntrance() {return this.entrance;}
    public PawnsMap getProfessors() {return this.professors;}

    /**returns the number of towers left in the schoolboard**/
    public int getSpareTowers(){
        return this.spareTowers;
    }

    /**1. adds the students in input to dining room
     * 2. moves professors if necessary
     * 3. removes coins from coinSupply in Game if there are enough coins
     * 4. returns the number of coins to add after the movement**/
    public int addToDiningRoom(PawnsMap pawns, Game game){
        ColourPawn pawnsList[] = ColourPawn.values();
        int num = 0;
        int coinsToAdd=0;
        for(ColourPawn colour : pawnsList){
            num = pawns.get(colour);
            if(num>0){
                for(int i=0; i<num; i++) {
                    this.diningRoom.add(colour);
                    if (this.diningRoom.get(colour) % 3 == 0) coinsToAdd ++;
                }
                this.verifyProfessorInfluence(game, colour);
            }
        }
        if(coinsToAdd <= game.getCoinSupply()) {
            game.removeCoins(coinsToAdd);
            return coinsToAdd;
        }else{
            coinsToAdd = game.getCoinSupply();
            game.removeCoins(coinsToAdd);
            return coinsToAdd;
        }
    }


    /** receives in input a cloud and inserts all the students in entrance and clear the students on the cloud**/
    public void insertCloud(Cloud cloud){this.entrance.add(cloud.clearCloud());}

    /**move students in input to the dining room and remove from
     * the entrance, return the number of coins to add after the movement **/
    public int fromEntranceToDiningRoom(PawnsMap toMove, Game game){
        int coinsToAdd=0;
        coinsToAdd = this.addToDiningRoom(toMove, game);
        this.entrance.remove(toMove);
        return coinsToAdd;
    }

    /**
     * Verifies and updates all the SchoolBoards:
     * move the professors from professorLeft map or other player's
     * schoolboard to the school board with more influence*/

    public void verifyProfessorInfluence(Game game, ColourPawn colour){
        if(game.getProfessorsLeft().get(colour) == 1){
            game.getProfessorsLeft().remove(colour);
            this.professors.add(colour);
            return;
        }
        for(Player p : game.getPlayers()){
            if(! (p.getSchoolBoard() == this)){
                if(!(p.getSchoolBoard().professors.get(colour)==0) &&  this.diningRoom.get(colour) > p.getSchoolBoard().diningRoom.get(colour)){
                    this.professors.add(colour);
                    p.getSchoolBoard().professors.remove(colour);}
            }
        }
    }

    public void setDiningRoom(PawnsMap diningRoom) {this.diningRoom = diningRoom;}
    public void setEntrance(PawnsMap entrance) {this.entrance = entrance;}
    public void setSpareTowers(int spareTowers) {this.spareTowers = spareTowers;}

    public void setProfessors(PawnsMap professors) {
        this.professors = professors;
    }
}


package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.ColourTower;

public class Island {

    //SIMPLE GAME ATTRIBUTES

    private boolean hasMotherNature;
    private PawnsMap students;
    private ColourTower towerColour;
    private int towerCount;

    //COMPLEX GAME ATTRIBUTES
    private int numNoEntryTile;

     public Island(){
         hasMotherNature=false;
         students=new PawnsMap();
         towerColour=null;
         towerCount=0;

         numNoEntryTile = 0;
     }

    public void setHasMotherNature(boolean hasMotherNature) {
         this.hasMotherNature = hasMotherNature;
    }

    //MODIFICA 5
    public boolean getHasMotherNature(){
         return hasMotherNature;
    }

    public int getTowerCount() {
        return towerCount;
    }

    public ColourTower getTowerColour() {
        return towerColour;
    }

    public void setTowerColor(ColourTower towerColour) {
        if (towerColour.equals(null)){towerCount++;}
        this.towerColour = towerColour;
    }

    public void addTower(int num){
         this.towerCount+=num;
    }

    public PawnsMap getStudents() {
         return this.students;
     }

    //MODIFICA 7: aggiunta del metodo:
    public void addStudents(PawnsMap toAdd){
        this.students.add(toAdd);
    }

    public void addStudents(ColourPawn colour, int num){
        this.students.add(colour, num);
    }

    public int getNumNoEntryTile() {
        return numNoEntryTile;
    }

    public void setNumNoEntryTile(int numNoEntryTile) {
        this.numNoEntryTile = numNoEntryTile;
    }

    /** calculates the most influent player on the island,
     * it works for 2/3-players game
     * @param game
     */
    public Player getInfluence(Game game){

         Player moreInfluent=null;
         int maxInfluence=0;
         int currScore;
         for(Player p: game.getPlayers()) {
             currScore = 0;
             for (ColourPawn professor : ColourPawn.values()) {
                 if(p.getSchoolBoard().getProfessors().get(professor)==1)
                 currScore += getStudents().get(professor);
             }
             if (p.getColourTower() == towerColour) {
                 currScore += towerCount;
             }

             if (currScore > 0 && currScore > maxInfluence) {
                 maxInfluence = currScore;
                 moreInfluent = p;
             }
         }

         return moreInfluent;
    }

    public Player getOwner(Game game){
        Player owner = null;
        for (Player p : game.getPlayers()){
            if (p.getColourTower().equals(getTowerColour())){
                owner = p;
            }
        }
        return owner;
    }

}
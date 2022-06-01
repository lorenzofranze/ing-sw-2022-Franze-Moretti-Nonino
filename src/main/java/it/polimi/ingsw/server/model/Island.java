package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.ColourTower;
import it.polimi.ingsw.common.gamePojo.IslandPojo;
import it.polimi.ingsw.common.gamePojo.PawnsMapPojo;

public class Island {

    private boolean hasMotherNature;
    private PawnsMap students;
    private ColourTower towerColour;
    private int towerCount;
    private int numNoEntryTile;

     public Island(){
         hasMotherNature=false;
         students=new PawnsMap();
         towerColour=null;
         towerCount=0;
         numNoEntryTile = 0;
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

    /**if a tower is placed on this island, getOwner returns the Player who owns that tower. Otherwise retruns null*/
    public Player getOwner(Game game){
        Player owner = null;
        for (Player p : game.getPlayers()){
            if (p.getColourTower().equals(getTowerColour())){
                owner = p;
            }
        }
        return owner;
    }

    /**returns a IslandPojo representing this*/
    public IslandPojo toPojo(){
        IslandPojo pojoIslandPojo = new IslandPojo();
        pojoIslandPojo.setHasMotherNature(this.hasMotherNature);
        pojoIslandPojo.setStudents(new PawnsMapPojo(this.students));
        pojoIslandPojo.setTowerColour(this.towerColour);
        pojoIslandPojo.setTowerCount(this.towerCount);
        pojoIslandPojo.setNumNoEntryTile(this.getNumNoEntryTile());
        return pojoIslandPojo;
    }

    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

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

    public void setTowerCount(int towerCount) {
        this.towerCount = towerCount;
    }

    public void addTower(int num){
        this.towerCount+=num;
    }

    public PawnsMap getStudents() {
        return this.students;
    }

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

}
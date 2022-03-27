package it.polimi.ingsw;

import java.util.HashMap;
import java.util.Map;

public class Island {

    //SIMPLE GAME ATTRIBUTES
    private int islandId;
    private boolean hasMotherNature;
    private PawnsMap students;
    private ColourTower towerColour;
    private int towerCount;

    //COMPLEX GAME ATTRIBUTES
    private boolean hasNoEntryTile;

     public Island(int islandId){
         this.islandId=islandId;
         hasMotherNature=false;
         students=new PawnsMap();
         towerColour=null;
         towerCount=0;

         hasNoEntryTile = false;
     }

    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public int getTowerCount() {
        return towerCount;
    }

    public ColourTower getTowerColour() {
        return towerColour;
    }

    public void setTowerColor(ColourTower towerColour) {
        this.towerColour = towerColour;
    }

    public void addTower(int num){
         this.towerCount+=num;
    }

    public PawnsMap getStudents() { return students;}

    public boolean HasNoEntryTile() {
        return hasNoEntryTile;
    }

    public void setHasNoEntryTile(boolean hasNoEntryTile) {
        this.hasNoEntryTile = hasNoEntryTile;
    }
}

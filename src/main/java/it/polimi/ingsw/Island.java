package it.polimi.ingsw;

import java.util.HashMap;
import java.util.Map;

public class Island {

    //SIMPLE GAME ATTRIBUTES

    private boolean hasMotherNature;
    private PawnsMap students;
    private ColourTower towerColour;
    private int towerCount;

    //COMPLEX GAME ATTRIBUTES
    private boolean hasNoEntryTile;

     public Island(){
         hasMotherNature=false;
         students=new PawnsMap();
         towerColour=null;
         towerCount=0;

         hasNoEntryTile = false;
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
        this.towerColour = towerColour;
    }

    public void addTower(int num){
         this.towerCount+=num;
    }

    public PawnsMap getStudents() { return students;}

    //MODIFICA 7: aggiunta del metodo:
    public void addStudents(ColourPawn colour, int number){
         students.addPawns(colour,number);
    }

    public boolean getHasNoEntryTile() {
        return hasNoEntryTile;
    }

    public void setHasNoEntryTile(boolean hasNoEntryTile) {
        this.hasNoEntryTile = hasNoEntryTile;
    }
}

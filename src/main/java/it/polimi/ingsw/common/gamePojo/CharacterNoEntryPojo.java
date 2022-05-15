package it.polimi.ingsw.common.gamePojo;

public class CharacterNoEntryPojo extends CharacterPojo{
    private int numNoEntry;

    public void setNumNoEntry(int numNoEntry){
        this.numNoEntry = numNoEntry;
    }

    public String toString(){
        String ris = "Character " + this.getCharacterId() + " price: " + this.getActualCost() +" -- num. of no entry tiles: " + numNoEntry;
        return  ris;
    }
}

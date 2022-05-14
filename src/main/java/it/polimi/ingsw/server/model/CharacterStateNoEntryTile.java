package it.polimi.ingsw.server.model;

public class CharacterStateNoEntryTile extends CharacterState{
    private int numNoEntry;

    public int getNumNoEntry() {
        return numNoEntry;
    }

    public void addNoEntryTile() {
        this.numNoEntry++;
    }

    public void removeNoEntryTile(){
        this.numNoEntry--;
    }

    public CharacterStateNoEntryTile(int characterId, int actualCost){
        super(characterId, actualCost);
    }
}

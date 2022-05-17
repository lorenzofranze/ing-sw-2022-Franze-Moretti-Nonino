package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CharacterNoEntryPojo;
import it.polimi.ingsw.common.gamePojo.CharacterPojo;

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

    @Override
    public CharacterNoEntryPojo toPojo(){
        CharacterNoEntryPojo characterNoEntryPojo = new CharacterNoEntryPojo();
        characterNoEntryPojo.setCharacterId(this.getCharacterId());
        characterNoEntryPojo.setActualCost(this.getCost());
        characterNoEntryPojo.setNumNoEntry(this.numNoEntry);
        return characterNoEntryPojo;
    }
}

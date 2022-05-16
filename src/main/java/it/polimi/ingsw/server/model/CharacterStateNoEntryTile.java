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
    public CharacterPojo toPojo(){
        CharacterPojo pojoCharacterPojo = new CharacterNoEntryPojo();
        pojoCharacterPojo.setCharacterId(this.getCharacterId());
        pojoCharacterPojo.setActualCost(this.getCost());
        ((CharacterNoEntryPojo)pojoCharacterPojo).setNumNoEntry(this.numNoEntry);

        return pojoCharacterPojo;
    }
}

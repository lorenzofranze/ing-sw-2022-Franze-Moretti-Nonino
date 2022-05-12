package it.polimi.ingsw.common.gamePojo;

import java.util.Objects;

public class CharacterPojo {

    private int characterId;
    private int actualCost;
    private boolean incremented;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getActualCost() {
        return actualCost;
    }

    public void setActualCost(int actualCost) {
        this.actualCost = actualCost;
    }

    public boolean isIncremented() {
        return incremented;
    }

    public void setIncremented(boolean incremented) {
        this.incremented = incremented;
    }

    @Override
    public String toString(){
        String ris = "Character " + characterId + " price: " + this.getActualCost();
        return  ris;
    }
}

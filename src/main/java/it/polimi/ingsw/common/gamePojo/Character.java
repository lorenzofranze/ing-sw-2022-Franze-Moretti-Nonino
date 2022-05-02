package it.polimi.ingsw.common.gamePojo;

import java.util.Objects;

public class Character{

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
}

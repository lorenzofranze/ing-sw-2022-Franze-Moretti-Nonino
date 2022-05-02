package it.polimi.ingsw.server.model;

import java.util.Objects;

public class Character{


    private int characterId;
    private int actualCost;
    private boolean incremented;

    public Character(int characterId, int actualCost) {
        this.characterId = characterId;
        this.actualCost = actualCost;
        this.incremented = false;
    }

    public int getCharacterId() {
        return characterId;
    }

    public int getCost() {
        return this.actualCost;
    }

    public void use() {
        if(!incremented) {
            this.actualCost++;
            this.incremented = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return characterId == character.characterId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }

    public it.polimi.ingsw.common.gamePojo.Character toPojo(){
        it.polimi.ingsw.common.gamePojo.Character pojoCharacter = new it.polimi.ingsw.common.gamePojo.Character();
        pojoCharacter.setCharacterId(this.getCharacterId());
        pojoCharacter.setActualCost(this.actualCost);
        pojoCharacter.setIncremented(this.incremented);
        return pojoCharacter;
    }
}

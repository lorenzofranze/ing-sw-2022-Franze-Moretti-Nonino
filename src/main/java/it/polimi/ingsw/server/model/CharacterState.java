package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CharacterPojo;
import it.polimi.ingsw.common.gamePojo.PawnsMapPojo;

import java.util.Objects;

public class CharacterState {

    private int characterId;
    private int actualCost;
    private boolean incremented;

    public CharacterState(int characterId, int actualCost) {
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

    public boolean isIncremented() {
        return incremented;
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
        CharacterState characterState = (CharacterState) o;
        return characterId == characterState.characterId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }

    /**returns the CharacterPojo representing this CharacterState*/
    public CharacterPojo toPojo(){
        CharacterPojo characterStudentPojo = new CharacterPojo();
        characterStudentPojo.setCharacterId(this.getCharacterId());
        characterStudentPojo.setActualCost(this.getCost());
        characterStudentPojo.setIncremented(this.isIncremented());
        characterStudentPojo.setStudents(null);
        characterStudentPojo.setNumNoEntry(null);
        return characterStudentPojo;
    }

}

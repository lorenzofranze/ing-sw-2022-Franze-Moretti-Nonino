package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CharacterPojo;
import it.polimi.ingsw.common.gamePojo.PawnsMapPojo;

import java.util.Objects;

public class CharacterState {

    private int characterId;
    private int actualCost;
    private boolean incremented;
    protected String description = "";

    /**
     * Each character card has an id, a cost (incremented or not) and a description different for each id
     * @param characterId
     * @param actualCost
     */
    public CharacterState(int characterId, int actualCost) {
        this.characterId = characterId;
        this.actualCost = actualCost;
        this.incremented = false;
        switch (characterId){
            case 1:
                description = "Choose one of the student on this card " +
                        "and place him on the island that you like the most ";
                break;
            case 2:
                description = "During this turn, you take control of any number of professors even if you have the " +
                        "same number of Students as the player who currently controls them.";
                break;
            case 3:
                description = "Choose an Island and resolve the Island as if Mother Nature has ended her movement there." +
                        " Remember mother nature will continue her steps as usual";
                break;
            case 4:
                description = "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant " +
                        "card you've played.";
                break;
            case 5:
                description = "Place a No Entry tile on an Island of your choice. Choose an island " +
                        " and the next time mother nature will stops on it, she will forget to calculate the influence on the " +
                        " island";
                break;
            case 6:
                description = "When resolving a Conquering on an Island, Towers do not count towards influence.";
                break;
            case 7:
                description = "You may take up to 3 Students from this card and replace them with the same number of " +
                        "Students from your Entrance. Select first the student on your entrance then the student on the card" +
                        ". Now select how many students you want to move";
                break;
            case 8:
                description = "During the influence calculation this turn, you count as having 2 more influence.";
                break;
            case 9:
                description = "Choose a color of Student: during the influence calculation this turn, that color " +
                        "adds no influence.";
                break;
            case 10:
                description = "You may exchange up to 2 Students between your Entrance and your Dining Room. " +
                        "First of all choose how many students you want to move, then select first the student on your " +
                        "entrance, then the student on your dining room";
                break;

            case 11:
                description = "Choose one of the student on this card " +
                        "and he will be moved in your entry ";
                break;
            case 12:
                description = "Choose a type of Student: every player (including yourself) must return 3 Students of " +
                        "that type from their Dining Room to the bag. If any player has fewer than 3 Students of that " +
                        "type, return as many Students as they have.";
                break;
        }
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

    /**sets character card used: increments cost and set true if first increment-> one coin doesn't return
     * in central reserve but remains on the card
     */
    public boolean use() {
        if(!incremented) {
            this.actualCost++;
            this.incremented = true;
            return true;
        }
        return false;
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

    /**
     * Returns the CharacterPojo representing this CharacterState*/
    public CharacterPojo toPojo(){
        CharacterPojo characterStudentPojo = new CharacterPojo();
        characterStudentPojo.setCharacterId(this.getCharacterId());
        characterStudentPojo.setActualCost(this.getCost());
        characterStudentPojo.setIncremented(this.isIncremented());
        characterStudentPojo.setDescription(description);
        characterStudentPojo.setStudents(null);
        characterStudentPojo.setNumNoEntry(null);
        return characterStudentPojo;
    }

    public void setIncremented(boolean incremented) {
        this.incremented = incremented;
    }
}

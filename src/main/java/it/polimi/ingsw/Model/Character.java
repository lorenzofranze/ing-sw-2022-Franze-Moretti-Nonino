package it.polimi.ingsw.Model;

import java.util.Objects;

public class Character implements java.io.Serializable{

    private static final long serialVersionUID = 884896313726842888L;

    private int characterId;
    private int actualCost;
    private boolean incremented;
    private AssistantCard asscard;
    public Character(){}

    public Character(int characterId, int actualCost) {
        this.characterId = characterId;
        this.actualCost = actualCost;
        this.incremented = false;
        this.asscard = new AssistantCard(5,6);
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

    public AssistantCard getAsscard() {
        return asscard;
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
}

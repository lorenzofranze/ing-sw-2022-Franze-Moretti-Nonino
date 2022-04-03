package it.polimi.ingsw.Model;

public class Character {

    private int characterId;
    private int defaultCost;
    private int incrementCost;

    public Character(int characterId, int defaultCost) {
        this.characterId = characterId;
        this.defaultCost = defaultCost;
        this.incrementCost = 0;
    }

    public int getCharacterId() {
        return characterId;
    }

    public int getCost() {
        return this.defaultCost + this.incrementCost;
    }

    public void increaseCost() {
        this.incrementCost = this.incrementCost + 1;
    }
}

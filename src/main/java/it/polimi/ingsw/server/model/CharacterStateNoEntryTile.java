package it.polimi.ingsw.server.model;
import it.polimi.ingsw.common.gamePojo.CharacterPojo;
import it.polimi.ingsw.common.gamePojo.PawnsMapPojo;

public class CharacterStateNoEntryTile extends CharacterState{
    private int numNoEntry;

    public CharacterStateNoEntryTile(int characterId, int actualCost){
        super(characterId, actualCost);
    }

    public int getNumNoEntry() {
        return numNoEntry;
    }

    public void addNoEntryTile() {
        this.numNoEntry++;
    }

    public void removeNoEntryTile(){
        this.numNoEntry--;
    }

    @Override
    public CharacterPojo toPojo(){
        CharacterPojo characterStudentPojo = new CharacterPojo();
        characterStudentPojo.setCharacterId(this.getCharacterId());
        characterStudentPojo.setActualCost(this.getCost());
        characterStudentPojo.setIncremented(this.isIncremented());
        characterStudentPojo.setStudents(null);
        characterStudentPojo.setNumNoEntry(this.numNoEntry);

        return characterStudentPojo;
    }
}

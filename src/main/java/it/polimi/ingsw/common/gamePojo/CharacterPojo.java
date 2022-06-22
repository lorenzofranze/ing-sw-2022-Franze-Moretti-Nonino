package it.polimi.ingsw.common.gamePojo;

import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.CharacterStateNoEntryTile;
import it.polimi.ingsw.server.model.CharacterStateStudent;

import java.util.Objects;

public class CharacterPojo {

    private int characterId;
    private int actualCost;
    private boolean incremented;
    private PawnsMapPojo students;
    private Integer numNoEntry;
    private String description;

    public void setNumNoEntry(Integer numNoEntry){
        this.numNoEntry = numNoEntry;
    }

    public Integer getNumNoEntry(){
        return this.numNoEntry;
    }

    public PawnsMapPojo getStudents() {
        return students;
    }

    public void setStudents(PawnsMapPojo students) {
        this.students = students;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        String ris = "Character " + characterId + " price: " + this.getActualCost();
        if (this.students != null){
            ris = ris + "  -- students: " + students.toString();
        }
        if(this.numNoEntry != null){
            ris = ris + "  -- num no entry tiles: " + this.numNoEntry;
        }
        ris = ris + "\n" + description;
        return  ris;
    }

    public CharacterState getCharacterState(){
        CharacterState characterState = null;
        switch(characterId){
            case 1: case 7: case 11:
                CharacterStateStudent characterStateStudent = new CharacterStateStudent(characterId, actualCost);
                characterStateStudent.setStudents(students.getPawnsMap());
                characterState = characterStateStudent;
                break;
            case 5:
                CharacterStateNoEntryTile characterStateNoEntryTile = new CharacterStateNoEntryTile(characterId, actualCost);
                characterStateNoEntryTile.setNumNoEntry(numNoEntry);
                characterState = characterStateNoEntryTile;
                break;
            default:
                characterState = new CharacterState(characterId, actualCost);
                break;
        }
        characterState.setIncremented(this.incremented);
        return characterState;
    }
}

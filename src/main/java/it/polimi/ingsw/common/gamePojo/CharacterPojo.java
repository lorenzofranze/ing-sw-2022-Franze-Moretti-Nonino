package it.polimi.ingsw.common.gamePojo;

import java.util.Objects;

public class CharacterPojo {

    private int characterId;
    private int actualCost;
    private boolean incremented;
    private PawnsMapPojo students;
    private Integer numNoEntry;

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

    @Override
    public String toString(){
        String ris = "Character " + characterId + " price: " + this.getActualCost();
        if (this.students != null){
            ris = ris + "  -- students: " + students.toString();
        }
        if(this.numNoEntry != null){
            ris = ris + "  -- num no entry tiles: " + this.numNoEntry;
        }
        return  ris;
    }
}

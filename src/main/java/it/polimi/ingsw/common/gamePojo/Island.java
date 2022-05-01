package it.polimi.ingsw.common.gamePojo;

public class Island {

    private boolean hasMotherNature;
    private PawnsMap students;
    private ColourTower towerColour;
    private int towerCount;
    private int numNoEntryTile;

    public boolean isHasMotherNature() {
        return hasMotherNature;
    }

    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public PawnsMap getStudents() {
        return students;
    }

    public void setStudents(PawnsMap students) {
        this.students = students;
    }

    public ColourTower getTowerColour() {
        return towerColour;
    }

    public void setTowerColour(ColourTower towerColour) {
        this.towerColour = towerColour;
    }

    public int getTowerCount() {
        return towerCount;
    }

    public void setTowerCount(int towerCount) {
        this.towerCount = towerCount;
    }

    public int getNumNoEntryTile() {
        return numNoEntryTile;
    }

    public void setNumNoEntryTile(int numNoEntryTile) {
        this.numNoEntryTile = numNoEntryTile;
    }
}

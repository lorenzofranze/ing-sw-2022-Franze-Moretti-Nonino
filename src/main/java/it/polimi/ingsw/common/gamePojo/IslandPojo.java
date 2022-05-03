package it.polimi.ingsw.common.gamePojo;

public class IslandPojo {

    private boolean hasMotherNature;
    private PawnsMapPojo students;
    private ColourTower towerColour;
    private int towerCount;
    private int numNoEntryTile;

    public boolean isHasMotherNature() {
        return hasMotherNature;
    }

    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public PawnsMapPojo getStudents() {
        return students;
    }

    public void setStudents(PawnsMapPojo students) {
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

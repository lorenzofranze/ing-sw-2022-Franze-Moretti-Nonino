package it.polimi.ingsw.common.gamePojo;

public class SchoolBoard {

    private PawnsMap professors;
    private PawnsMap diningRoom;
    private PawnsMap entrance;
    private int spareTowers;

    public PawnsMap getProfessors() {
        return professors;
    }

    public void setProfessors(PawnsMap professors) {
        this.professors = professors;
    }

    public PawnsMap getDiningRoom() {
        return diningRoom;
    }

    public void setDiningRoom(PawnsMap diningRoom) {
        this.diningRoom = diningRoom;
    }

    public PawnsMap getEntrance() {
        return entrance;
    }

    public void setEntrance(PawnsMap entrance) {
        this.entrance = entrance;
    }

    public int getSpareTowers() {
        return spareTowers;
    }

    public void setSpareTowers(int spareTowers) {
        this.spareTowers = spareTowers;
    }
}

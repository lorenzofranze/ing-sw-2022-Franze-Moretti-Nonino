package it.polimi.ingsw.common.gamePojo;

import it.polimi.ingsw.server.model.SchoolBoard;

public class SchoolBoardPojo {

    private PawnsMapPojo professors;
    private PawnsMapPojo diningRoom;
    private PawnsMapPojo entrance;
    private int spareTowers;

    public PawnsMapPojo getProfessors() {
        return professors;
    }

    public void setProfessors(PawnsMapPojo professors) {
        this.professors = professors;
    }

    public PawnsMapPojo getDiningRoom() {
        return diningRoom;
    }

    public void setDiningRoom(PawnsMapPojo diningRoom) {
        this.diningRoom = diningRoom;
    }

    public PawnsMapPojo getEntrance() {
        return entrance;
    }

    public void setEntrance(PawnsMapPojo entrance) {
        this.entrance = entrance;
    }

    public int getSpareTowers() {
        return spareTowers;
    }

    public void setSpareTowers(int spareTowers) {
        this.spareTowers = spareTowers;
    }

    /**starting form this SchoolBoardPojo, it returns a SchoolBoard with the same features*/
    public SchoolBoard getSchoolBoard(){
        SchoolBoard schoolBoard = new SchoolBoard();
        schoolBoard.setSpareTowers(spareTowers);
        schoolBoard.setProfessors(professors.getPawnsMap());
        schoolBoard.setDiningRoom(diningRoom.getPawnsMap());
        schoolBoard.setEntrance(entrance.getPawnsMap());
        return schoolBoard;
    }

    @Override
    public boolean equals(Object o) {
        SchoolBoardPojo o1;
        if (o == null) {
            return false;
        }
        if (o instanceof SchoolBoardPojo) {
            o1 = (SchoolBoardPojo) o;
        } else {
            return false;
        }

        if (!this.professors.equals(o1.professors)) {
            return false;
        }
        if (!this.diningRoom.equals(o1.diningRoom)) {
            return false;
        }
        if (!this.entrance.equals(o1.entrance)) {
            return false;
        }
        if (this.spareTowers != o1.spareTowers) {
            return false;
        }
        return true;
    }
}

package it.polimi.ingsw.common.gamePojo;

public class CharacterStudentPojo extends CharacterPojo{
    private PawnsMapPojo students;

    public PawnsMapPojo getStudents() {
        return students;
    }

    public void setStudents(PawnsMapPojo students) {
        this.students = students;
    }

    public String toString(){
        String ris = "Character " + this.getCharacterId() + " price: " + this.getActualCost() +" -- " + students.toString();
        return  ris;
    }


}

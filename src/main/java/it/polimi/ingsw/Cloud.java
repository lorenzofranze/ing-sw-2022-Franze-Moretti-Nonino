package it.polimi.ingsw;

import java.util.*;

public class Cloud {

    private PawnsMap students;
    private int cloudId;

    public Cloud(int cloudId){
        this.cloudId=cloudId;
        students=new PawnsMap();
    }

    public int getCloudId() {
        return cloudId;
    }

    /** @return PawnsMap all the students on the cloud
     */
    public PawnsMap getStudents() {
        return students;
    }

    //MODIFICA 2
    public void addStudent(ColourPawn newStudent){
        students.addPawn(newStudent);
    }

    //MODIFICA 3

    /** removes all the students from the cloud
     * @return PawnsMap the students that were situated on the cloud
     */
    public PawnsMap clearCloud(){
        PawnsMap oldStudents= new PawnsMap();
        for (ColourPawn c : ColourPawn.values()) {
            oldStudents.addPawns(c,students.getPawns(c));
        }
        int numPawn;
        for (ColourPawn c: ColourPawn.values()) {
            numPawn=students.getPawns(c);
            students.removePawns(c,numPawn);
        }
        return oldStudents;
    }
}

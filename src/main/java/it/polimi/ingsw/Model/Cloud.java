package it.polimi.ingsw.Model;

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

    /** @return the clone of the cloud PawnsMap*/
    public PawnsMap getStudents() {
        return this.students.clone();
    }

    //MODIFICA 2
    public void addStudent(ColourPawn newStudent){
        students.add(newStudent);
    }

    //MODIFICA 3

    /** removes all the students from the cloud
     * @return PawnsMap the students that were situated on the cloud
     */
    public PawnsMap clearCloud(){
        PawnsMap oldStudents = this.getStudents();
        this.students = new PawnsMap();
        return oldStudents;
    }
}

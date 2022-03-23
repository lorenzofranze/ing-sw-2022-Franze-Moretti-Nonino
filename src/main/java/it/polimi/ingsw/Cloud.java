package it.polimi.ingsw;

import java.util.HashMap;
import java.util.Map;

public class Cloud {
    private Map<ColourPawn, Integer> students;
    private Integer cloudId;

    public Cloud(Integer cloudId){
        this.cloudId=cloudId;
        students=new HashMap<>();
    }

    public Integer getCloudId() {
        return cloudId;
    }

    public void addStudents(Map<ColourPawn, Integer> students) {
        this.students = students;
    }

    public void removeStudents(){
        students.clear();
    }

    public Integer countStudents(ColourPawn students){
        return this.students.get(students);
    }
}

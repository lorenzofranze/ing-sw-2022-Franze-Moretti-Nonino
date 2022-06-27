package it.polimi.ingsw.common.gamePojo;

import it.polimi.ingsw.server.model.Cloud;

public class CloudPojo {

    private PawnsMapPojo students;
    private int cloudId;

    public PawnsMapPojo getStudents() {
        return students;
    }

    public void setStudents(PawnsMapPojo students) {
        this.students = students;
    }

    public int getCloudId() {
        return cloudId;
    }

    public void setCloudId(int cloudId) {
        this.cloudId = cloudId;
    }

    public Cloud getCloud(){
        Cloud cloud = new Cloud(cloudId);
        cloud.getStudents().add(students.getPawnsMap());
        return cloud;
    }

    @Override
    public boolean equals(Object o){
        CloudPojo o1;
        if (o == null){
            return false;
        }
        if (o instanceof CloudPojo){
            o1 = (CloudPojo) o;
        }else{
            return false;
        }

        if (this.cloudId != o1.cloudId){
            return false;
        }
        if (!this.students.equals(o1.students)){
            return false;
        }

        return true;

    }
}

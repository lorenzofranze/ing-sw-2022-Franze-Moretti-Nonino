package it.polimi.ingsw;

import java.util.HashMap;
import java.util.Map;

public class Island {
    private Integer islandId;
    private boolean hasMotherNature;
    private Map<ColourPawn, Integer> students;
    private ColourTower towerColour;
    private Integer towerCount;

     public Island(Integer islandId){
         this.islandId=islandId;
         hasMotherNature=false;
         students=new HashMap<>();
         towerColour=null;
         towerCount=0;
     }

    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }


    public Integer getTowerCount() {
        return towerCount;
    }

    public ColourTower getTowerColour() {
        return towerColour;
    }

    public void setTowerColor(ColourTower towerColour) {
        this.towerColour = towerColour;
    }

    public void addTower(Integer towerNum){
         this.towerCount+=towerNum;
    }

    public void addStudents(ColourPawn studentColour, Integer num){
         students.put(studentColour, num+students.get(studentColour));
    }

    public Integer getStudentCount(ColourPawn studentColour){
         return students.get(studentColour);
    }

}

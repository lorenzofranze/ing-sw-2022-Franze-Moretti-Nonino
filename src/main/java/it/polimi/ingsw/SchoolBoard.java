package it.polimi.ingsw;

import java.util.*;

public class SchoolBoard {
    private Set<ColourPawn> professors;
    private Map<ColourPawn, Integer> students;
    private int numberOfTowers;

    /** initializes values and set numberOfTowers to 8,
     * if there are 3 players
     * the value will be decremented to 6
     */
    public SchoolBoard(){
        this.professors = new HashSet<>();
        this.students = new HashMap<>();
        ColourPawn pawns[] = ColourPawn.values(); // array of enum elements
        for(ColourPawn p : pawns){
            students.put(p, 0);
        }
        this.numberOfTowers = 8;
    }
    /** add professor to schoolboard **/
    public void addProfessor(ColourPawn prof){
        professors.add(prof);
    }
    /** remove professor from schoolboard **/
    public void removeProfessor(ColourPawn prof){
        professors.remove(prof);
    }
    /** boolean if the schoolboard contsins the professor in input **/
    public boolean ifPresentProfessor(ColourPawn prof){
        if (professors.contains(prof))
            return true;
        return false;
    }
    /** increment the number of students for a colour **/
    public void addStudents(ColourPawn colour, int num){
        students.put(colour, students.get(colour)+num);
    }
    /** return the number of students for a colour **/
    public int countStudents(ColourPawn colour){
        return students.get(colour);
    }
    /** add a tower to the schoolboard**/
    public void addTower(){
        this.numberOfTowers ++;
    }

    /** remove a tower from the schoolboard,
     * in case of 3 players the method can be used to set
     * the number of towers to 6
     */
    public void removeTower() {
        this.numberOfTowers --;
    }

}

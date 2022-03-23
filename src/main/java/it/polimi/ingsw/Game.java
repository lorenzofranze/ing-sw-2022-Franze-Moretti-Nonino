package it.polimi.ingsw;

import java.util.*;

public class Game {
    private List<Player> players;
    private List<Island> islands;
    private Map<ColourPawn, Integer> studentsBag;
    private List<Cloud> clouds;
    private List<AssistantCard> playedInThisTurn;
    private Set<ColourPawn> professorsLeft;

    public Game(List<Player> players){
        this.players=players;

        this.clouds=new ArrayList<Cloud>(players.size());
        for(Integer i=0; i<players.size(); i++){
            this.clouds.add(new Cloud(i));
        }


        this.islands=new ArrayList<Island>(12);
        for(Integer i=0; i<12; i++){
            this.islands.add(new Island(i));
        }


        this.studentsBag= new HashMap<ColourPawn, Integer>(5);
        for (ColourPawn currColor: ColourPawn.values()){
            this.studentsBag.put(currColor,26);
        }

        this.playedInThisTurn=new ArrayList<AssistantCard>(40);


        this.professorsLeft= new HashSet<ColourPawn>(5);
        for (ColourPawn currColor: ColourPawn.values()){
            this.professorsLeft.add(currColor);
        }

    }

    public void removeStudents(ColourPawn students, Integer num){
        Integer newValue= this.studentsBag.get(students)-num;
        this.studentsBag.put(students,newValue);
    }

    public List<Cloud> getCloud(){
        return clouds;
    }

    public void addPlayedInThisTurn(AssistantCard card){
        playedInThisTurn.add(card);
    }

    public void resetPlayedInThisTurn(){
        playedInThisTurn.clear();
    }

    public void unifyIsland(List<Island> toUnify){
    toUnify.get(0).setHasMotherNature(true);
    toUnify.get(0).addTower(toUnify.get(1).getTowerCount());

    for (ColourPawn currColor: ColourPawn.values()){
        toUnify.get(0).addStudents(currColor, toUnify.get(1).getStudentCount(currColor));
    }
    islands.remove(toUnify.get(1));
    }

    public Integer countStudents(ColourPawn students){
        return studentsBag.get(students);
    }

}

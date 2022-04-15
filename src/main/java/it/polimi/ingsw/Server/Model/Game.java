package it.polimi.ingsw.Server.Model;

import java.util.*;
import java.util.stream.Collectors;


public class Game {

    //SIMPLE GAME ATTRIBUTES
    private Integer gameId;
    private List<Player> players;
    private List<Island> islands;
    private PawnsMap studentsBag;
    private List<Cloud> clouds;
    private PawnsMap professorsLeft;

    //EXPERT GAME ATTRIBUTES
    /**keeps track of the effect used by the current player. It represents the characterId
     * of the character used*/
    private Character activeEffect;
    private int coinSupply;
    /**I can place the three Character-Cards on the table also if the expert-mode is off: no one in a simple-play
     * will use them since they won't have any personal coin
     */
    private List<Character> characters;



    //SIMPLE GAME METHODS

    /**creates 12 islands; creates n clouds where n is the lenght of List<Player>;
     * fills studentBag with 130 students; fills professorsLeft with all 5 professors;
     * in case of complex game fills coin supply with 20 and selects 3 character randomly*/
    public Game(List<String> players, Integer gameId){
        ArrayList<ColourTower> colourTowers= new ArrayList<>();
        //ATTENZIONE
        colourTowers.addAll(Arrays.stream(ColourTower.values()).collect(Collectors.toList()));
        ArrayList<ColourWizard> colourWizards= new ArrayList<>();
        //ATTENZIONE
        colourWizards.addAll(Arrays.stream(ColourWizard.values()).collect(Collectors.toList()));
        this.players=new ArrayList<Player>();
        for( int i=0; i<players.size(); i++){
            Player p= new Player(players.get(i), colourTowers.get(i), colourWizards.get(i));
            this.players.add(p);
        }


        this.clouds=new ArrayList<Cloud>(players.size());
        for(Integer i=0; i<players.size(); i++){
            this.clouds.add(new Cloud(i));
        }

        this.islands=new ArrayList<Island>(12);
        for(Integer i=0; i<12; i++){
            this.islands.add(new Island());
        }

        this.studentsBag= new PawnsMap();
        for (ColourPawn currColor: ColourPawn.values()){
            this.studentsBag.add(currColor,26);
        }

        this.professorsLeft= new PawnsMap();
        for (ColourPawn currColor: ColourPawn.values()){
            this.professorsLeft.add(currColor);
        }


            List<Character>temp = new ArrayList<Character>(12);

            temp.add(new Character(1, 1));
            temp.add(new Character(2, 2));
            temp.add(new Character(3, 3));
            temp.add(new Character(4, 1));
            temp.add(new Character(5, 2));
            temp.add(new Character(6, 3));
            temp.add(new Character(7, 1));
            temp.add(new Character(8, 2));
            temp.add(new Character(9, 3));
            temp.add(new Character(10, 1));
            temp.add(new Character(11, 2));
            temp.add(new Character(12, 3));

            Collections.shuffle(temp);

            characters = new ArrayList<Character>(3);

            characters.add(temp.get(1));
            characters.add(temp.get(2));
            characters.add(temp.get(3));

            this.coinSupply = 20;

            this.gameId=gameId;

    }

    public PawnsMap getStudentsBag() {
        return studentsBag;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Cloud> getClouds(){
        return clouds;
    }

    public void unifyIslands(List<Island> toUnify){
        toUnify.get(0).setHasMotherNature(true);

        for(int i = 1; i<toUnify.size(); i++){
            toUnify.get(0).addTower(toUnify.get(i).getTowerCount());
            toUnify.get(0).addStudents(toUnify.get(i).getStudents());
            islands.remove(toUnify.get(i));
        }
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    //public void addStudents(PawnsMap toAdd){studentsBag.add(toAdd);}

    //public void removeStudents(PawnsMap toAdd){studentsBag.remove(toAdd);}

    //public PawnsMap removeStudentsRandomly(int num){return studentsBag.removeRandomly(num);}

    //EXPERT GAME METHODS

    public void addCoins(int num){
        this.coinSupply = this.coinSupply + num;
    }

    public void removeCoins(int num){
        this.coinSupply = this.coinSupply - num;
    }

    /**returns the set of all the possible characters*/
    public List<Character> getCharacters() {
        return characters;
    }

    public Character getActiveEffect() {
        return activeEffect;
    }

    public void setActiveEffect(Character activeEffect) {
        this.activeEffect = activeEffect;
    }


    public List<Island> getIslands() {
        return islands;
    }

    public int getCoinSupply(){
        return coinSupply;
    }

    public PawnsMap getProfessorsLeft(){
        return professorsLeft;
    }

    public String islandsToString(){
        String islandString = "";
        int j = 0;
        for (Island i : islands){
            islandString = islandString +"Island number " + j + "\t- " + i.getStudents().toString() + "\t- owner: ";
            if (i.getOwner(this) == null) {
                islandString = islandString + "none";
            }else{
                islandString = islandString + i.getOwner(this).toString();
            }
            islandString = islandString + "\t- towers: " + i.getTowerCount() + "\n";
            j++;
        }
        return islandString;
    }


    /**returns the index of the island where MotherNature is placed (-1 MotherNature not found)*/
    public int findMotherNature(){
        int j = 0;
        int foundAt = -1;
        for(Island i: islands){
            if (i.getHasMotherNature() == true){
                foundAt = j;
            }
            j++;
        }
        return foundAt;
    }

    public String cloudsToString(){
        String cloudsString = "";
        int j = 0;
        for (Cloud c : clouds){
            if (!c.getStudents().isEmpty()) {
                cloudsString = cloudsString + "Cloud number " + j + " - " + c.getStudents().toString() + "\n";
            }
            else{
                cloudsString = cloudsString + "Cloud number " + j + " - empty\n";
            }
            j++;
        }
        return cloudsString;
    }
}

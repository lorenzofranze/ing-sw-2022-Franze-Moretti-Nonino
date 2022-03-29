package it.polimi.ingsw;

import java.util.*;

/** Game table class */
public class Game {

    //SIMPLE GAME ATTRIBUTES
    private String gameId;
    private boolean expert;
    private List<Player> players;
    private Player currentPlayer;
    private List<Island> islands;
    private PawnsMap studentsBag;
    private List<Cloud> clouds;
    private PawnsMap professorsLeft;

    //EXPERT GAME ATTRIBUTES
    /**keeps track of the effect used by the current player. It represents the characterId
     * of the character used*/
    private Character activeEffect;
    private int coinSupply;
    private List<Character> characters;



    //SIMPLE GAME METHODS

    /**creates 12 islands; creates n clouds where n is the lenght of List<Player>;
     * fills studentBag with 130 students; fills professorsLeft with all 5 professors;
     * in case of complex game fills coin supply with 20 and selects 3 character randomly*/
    public Game(List<Player> players, boolean expert){

        this.players=players;
        this.expert=expert;

        this.clouds=new ArrayList<Cloud>(players.size());
        for(Integer i=0; i<players.size(); i++){
            this.clouds.add(new Cloud(i));
        }

        this.islands=new ArrayList<Island>(12);
        for(Integer i=0; i<12; i++){
            this.islands.add(new Island(i));
        }

        this.studentsBag= new PawnsMap();
        for (ColourPawn currColor: ColourPawn.values()){
            this.studentsBag.addPawns(currColor,26);
        }

        this.professorsLeft= new PawnsMap();
        for (ColourPawn currColor: ColourPawn.values()){
            this.professorsLeft.addPawn(currColor);
        }

        if (expert = true) {

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
        }
    }

    public PawnsMap getStudentsBag() {
        return studentsBag;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Cloud> getClouds(){
        return clouds;
    }

    /** moves all the things of the second island on the first island
     * and then removes the second island
     * @param toUnify list of two islands
    */
    public void unifyIslands(List<Island> toUnify){
    toUnify.get(0).setHasMotherNature(true);
    toUnify.get(0).addTower(toUnify.get(1).getTowerCount());

    for (ColourPawn currColor: ColourPawn.values()){
            toUnify.get(0).getStudents().addPawns(currColor, toUnify.get(1).getStudents().getPawns(currColor));
        }
    islands.remove(toUnify.get(1));
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    //EXPERT GAME METHODS

    public void addCoins(int num){
        if(expert==true)
        {
            this.coinSupply = this.coinSupply + num;
        }
    }

    public void removeCoins(int num){
        if(expert==true){
            this.coinSupply = this.coinSupply - num;
            if(coinSupply<0) coinSupply=0;
        }

    }

    public int getCoinSupply(){
        if(expert==true){
            return coinSupply;
        }
        else return 0;
    }

    /**returns the set of all the possible characters*/
    public List<Character> getCharacters() {
        if(expert==true) {
            return characters;
        }
        else return null;
    }

    public Character getActiveEffect() {
        if(expert==true){
            return activeEffect;
        }
        else return null;

    }

    public void setActiveEffect(Character activeEffect) {
        if(expert==true){
            this.activeEffect = activeEffect;
        }
    }

    public boolean isExpert() {
        return expert;
    }

    public List<Island> getIslands() {
        return islands;
    }
}

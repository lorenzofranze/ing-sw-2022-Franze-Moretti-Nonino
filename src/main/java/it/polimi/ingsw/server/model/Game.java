package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.ColourTower;
import it.polimi.ingsw.common.gamePojo.ColourWizard;

import java.util.*;
import java.util.stream.Collectors;


public class Game {

    private Integer gameId;
    private List<Player> players;
    private List<Island> islands;
    private PawnsMap studentsBag;
    private List<Cloud> clouds;
    private PawnsMap professorsLeft;
    private int coinSupply;

    /**keeps track of the effect used by the current player. It represents the characterId
     * of the character used*/
    private CharacterState activeEffect;

    /**I can place the three Character-Cards on the table also if the expert-mode is off: no one in a simple-play
     * will use them since they won't have any personal coin
     */
    private List<CharacterState> characterStates;

    /**creates 12 islands; creates n clouds where n is the lenght of List<Player>;
     * fills studentBag with 130 students; fills professorsLeft with all 5 professors;
     * in case of complex game fills coin supply with 20 and selects 3 character randomly*/
    public Game(List<String> players, Integer gameId){
        ArrayList<ColourTower> colourTowers= new ArrayList<>();
        colourTowers.addAll(Arrays.stream(ColourTower.values()).collect(Collectors.toList()));
        ArrayList<ColourWizard> colourWizards= new ArrayList<>();
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

        List<CharacterState> temp = new ArrayList<CharacterState>();

        temp.add(new CharacterStateStudent(1, 1));
        temp.add(new CharacterState(2, 2));
        temp.add(new CharacterState(3, 3));
        temp.add(new CharacterState(4, 1));
        temp.add(new CharacterStateNoEntryTile(5, 2));
        temp.add(new CharacterState(6, 3));
        temp.add(new CharacterStateStudent(7, 1));
        temp.add(new CharacterState(8, 2));
        temp.add(new CharacterState(9, 3));
        temp.add(new CharacterState(10, 1));
        temp.add(new CharacterStateStudent(11, 2));
        temp.add(new CharacterState(12, 3));

        characterStates = new ArrayList<>();
        characterStates.add(temp.get(2));
        characterStates.add(temp.get(5));
        characterStates.add(temp.get(8));

        Collections.shuffle(temp);

        //characterStates.add(temp.get(0));
        //characterStates.add(temp.get(1));
        //characterStates.add(temp.get(2));


        this.coinSupply = 20;
        this.gameId = gameId;
    }

    /**receives a List of Islands to unify.
     * Places mother nature, all the towers and the students on the first island and deletes the others.*/
    public void unifyIslands(List<Island> toUnify){
        toUnify.get(0).setHasMotherNature(true);
        for(int i = 1; i<toUnify.size(); i++){
            toUnify.get(0).addTower(toUnify.get(i).getTowerCount());
            toUnify.get(0).addStudents(toUnify.get(i).getStudents());
            toUnify.get(0).setNumNoEntryTile(toUnify.get(0).getNumNoEntryTile() + toUnify.get(i).getNumNoEntryTile());
            islands.remove(toUnify.get(i));
        }
    }

    /**returns a string that describes all the islands (pawns, towers, motherNature)*/
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

    /**returns the index of the island where MotherNature is placed (-1 MotherNature not found)**/
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

    /**returns a string that describes all the clouds (pawns, empty or not)*/
    public String cloudsToString(){
        String cloudsString = "";
        int j = 0;
        for (Cloud c : clouds){
            if (!c.getStudents().isEmpty()) {cloudsString = cloudsString + "Cloud number " + j + " - " + c.getStudents().toString() + "\n";}
            else{ cloudsString = cloudsString + "Cloud number " + j + " - empty\n";}
            j++;
        }
        return cloudsString;
    }

    /**returns the Island object corresponding to the index insert as a parameter*/
    public Island getIslandOfIndex(int i){
        Island ris;
        if (i >= islands.size() || i < 0) return null;
        ris = islands.get(i);
        return ris;
    }

    /**checks that the players have the correct Professors according to their dining room. If not,
     * reassigns the professors correctly**/
    public void reassignProfessors(){
        for(ColourPawn c : ColourPawn.values()){
            Player owner = null;
            int pawnsDining = 0;
            for(Player p : players){
                if (pawnsDining < p.getSchoolBoard().getDiningRoom().get(c)){
                    owner = p;
                    pawnsDining = p.getSchoolBoard().getDiningRoom().get(c);
                }
            }

            if (owner == null){
                for(Player p : players){
                    p.getSchoolBoard().getProfessors().setNumberForColour(c, 0);
                }
                this.professorsLeft.setNumberForColour(c, 1);
            }else{
                owner.getSchoolBoard().getProfessors().setNumberForColour(c, 1);
                for(Player p : players){
                    if (!p.equals(owner))
                    p.getSchoolBoard().getProfessors().setNumberForColour(c, 0);
                }
                this.professorsLeft.setNumberForColour(c, 0);
            }
        }
    }

    /**returns the CaracterState of the character whose index is equal to the parameter**/
    public CharacterState getCharacterStateByID(int id){
        CharacterState ris = null;
        for(CharacterState characterState : characterStates) {
            if (characterState.getCharacterId() == id)
                ris = characterState;
        }
        return ris;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public void addCoins(int num){
        this.coinSupply = this.coinSupply + num;
    }

    public void removeCoins(int num){
        this.coinSupply = this.coinSupply - num;
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

    /**returns the set of all the possible characters**/
    public List<CharacterState> getCharacters() {return characterStates;}

    public CharacterState getActiveEffect() {return activeEffect;}

    public void setActiveEffect(CharacterState activeEffect) {this.activeEffect = activeEffect;}

    public List<Island> getIslands() {return islands;}

    public int getCoinSupply(){return coinSupply;}

    public PawnsMap getProfessorsLeft(){return professorsLeft;}

    public void setProfessorsLeft(PawnsMap professorsLeft) {this.professorsLeft = professorsLeft;}

    public void setCoinSupply(int coinSupply) {this.coinSupply = coinSupply;}

    public void setCharacterStates(List<CharacterState> characterStates) {
        this.characterStates = characterStates;
    }

    public void setIslands(List<Island> islands) {
        this.islands = islands;
    }

    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }
}

package it.polimi.ingsw.common.gamePojo;

import java.util.List;

public class GameState {

    private Player currentPlayer;
    private Character activeEffect;

    private int coinSupply;
    private List<Island> islands;
    private PawnsMap studentsBag;
    private List<Cloud> clouds;
    private PawnsMap professorsLeft;
    private List<Character> characters;

    private List<Player> players;

    public GameState(it.polimi.ingsw.server.model.Game game){
        for(it.polimi.ingsw.server.model.Player p : game.getPlayers()){

        }



    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Character getActiveEffect() {
        return activeEffect;
    }

    public void setActiveEffect(Character activeEffect) {
        this.activeEffect = activeEffect;
    }

    public int getCoinSupply() {
        return coinSupply;
    }

    public void setCoinSupply(int coinSupply) {
        this.coinSupply = coinSupply;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public void setIslands(List<Island> islands) {
        this.islands = islands;
    }

    public PawnsMap getStudentsBag() {
        return studentsBag;
    }

    public void setStudentsBag(PawnsMap studentsBag) {
        this.studentsBag = studentsBag;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    public PawnsMap getProfessorsLeft() {
        return professorsLeft;
    }

    public void setProfessorsLeft(PawnsMap professorsLeft) {
        this.professorsLeft = professorsLeft;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}

package it.polimi.ingsw.common.gamePojo;

import it.polimi.ingsw.server.controller.logic.GamePhase;

import java.util.List;

public class GameState {

    private Integer gameId;
    private String currentPlayer;
    private Character activeEffect;

    private int coinSupply;
    private List<Island> islands;
    private PawnsMap studentsBag;
    private List<Cloud> clouds;
    private PawnsMap professorsLeft;
    private List<Character> characters;

    private List<Player> players;



    private Phase currentPhase;
    private boolean gameOver;
    private String winner;
    private boolean expert;

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }



    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
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

    public void show(){

    }
}

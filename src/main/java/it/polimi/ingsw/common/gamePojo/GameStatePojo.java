package it.polimi.ingsw.common.gamePojo;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;

public class GameStatePojo {

    private Integer gameId;
    private PlayerPojo currentPlayerPojo;
    private CharacterPojo activeEffect;

    private int coinSupply;
    private List<IslandPojo> islandPojos;
    private PawnsMapPojo studentsBag;
    private List<CloudPojo> cloudPojos;
    private PawnsMapPojo professorsLeft;
    private List<CharacterPojo> characterPojos;

    private List<PlayerPojo> playerPojos;



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



    public PlayerPojo getCurrentPlayer() {
        return currentPlayerPojo;
    }

    public void setCurrentPlayer(PlayerPojo currentPlayerPojo) {
        this.currentPlayerPojo = currentPlayerPojo;
    }

    public CharacterPojo getActiveEffect() {
        return activeEffect;
    }

    public void setActiveEffect(CharacterPojo activeEffect) {
        this.activeEffect = activeEffect;
    }

    public int getCoinSupply() {
        return coinSupply;
    }

    public void setCoinSupply(int coinSupply) {
        this.coinSupply = coinSupply;
    }

    public List<IslandPojo> getIslands() {
        return islandPojos;
    }

    public void setIslands(List<IslandPojo> islandPojos) {
        this.islandPojos = islandPojos;
    }

    public PawnsMapPojo getStudentsBag() {
        return studentsBag;
    }

    public void setStudentsBag(PawnsMapPojo studentsBag) {
        this.studentsBag = studentsBag;
    }

    public List<CloudPojo> getClouds() {
        return cloudPojos;
    }

    public void setClouds(List<CloudPojo> cloudPojos) {
        this.cloudPojos = cloudPojos;
    }

    public PawnsMapPojo getProfessorsLeft() {
        return professorsLeft;
    }

    public void setProfessorsLeft(PawnsMapPojo professorsLeft) {
        this.professorsLeft = professorsLeft;
    }

    public List<CharacterPojo> getCharacters() {
        return characterPojos;
    }

    public void setCharacters(List<CharacterPojo> characterPojos) {
        this.characterPojos = characterPojos;
    }

    public List<PlayerPojo> getPlayers() {
        return playerPojos;
    }

    public void setPlayers(List<PlayerPojo> playerPojos) {
        this.playerPojos = playerPojos;
    }

    public Game getGame(){
        List<String> playersNicknames = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        for (PlayerPojo p: playerPojos){
            playersNicknames.add(p.getNickname());
            players.add(p.getPlayer());
        }
        Game game = new Game(playersNicknames, gameId);
        game.setPlayers(players);

        List<Island> islands = new ArrayList<>();
        for (IslandPojo i : islandPojos){
            islands.add(i.getIsland());
        }
        game.setIslands(islands);

        game.setStudentsBag(studentsBag.getPawnsMap());

        List<Cloud> clouds = new ArrayList<>();
        for (CloudPojo c: cloudPojos){
            clouds.add(c.getCloud());
        }
        game.setClouds(clouds);

        game.setProfessorsLeft(professorsLeft.getPawnsMap());

        game.setCoinSupply(coinSupply);

        if (activeEffect != null){
            game.setActiveEffect(activeEffect.getCharacterState());
        }

        List<CharacterState> characterStates = new ArrayList<>();
        for (CharacterPojo c: characterPojos){
            characterStates.add(c.getCharacterState());
        }
        game.setCharacterStates(characterStates);

        return game;

    }

    public boolean equals(Object o){
        GameStatePojo o1;
        if (o == null){
            return false;
        }
        if (o instanceof GameStatePojo){
            o1 = (GameStatePojo) o;
        }else{
            return false;
        }

        if (this.gameId != o1.gameId){
            return false;
        }

        if (this.gameOver != o1.gameOver){
            return false;
        }

        if (this.expert != o1.expert){
            return false;
        }

        if (this.coinSupply != o1.coinSupply){
            return false;
        }

        if (this.winner != null){
            if (o1.winner != null){
                if (!this.winner.equals(o1.winner)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.winner != null){
                return false;
            }
        }

        if (this.currentPhase != null){
            if (o1.currentPhase != null){
                if (!this.currentPhase.equals(o1.currentPhase)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.currentPhase != null){
                return false;
            }
        }

        if (this.activeEffect != null){
            if (o1.activeEffect != null){
                if (!this.activeEffect.equals(o1.activeEffect)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.activeEffect != null){
                return false;
            }
        }

        if (this.studentsBag != null){
            if (o1.studentsBag != null){
                if (!this.studentsBag.equals(o1.studentsBag)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.studentsBag != null){
                return false;
            }
        }


        if (this.professorsLeft != null){
            if (o1.professorsLeft != null){
                if (!this.professorsLeft.equals(o1.professorsLeft)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.professorsLeft != null){
                return false;
            }
        }

        if (this.islandPojos != null){
            if (o1.islandPojos != null){
                if (!this.islandPojos.equals(o1.islandPojos)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.islandPojos != null){
                return false;
            }
        }

        if (this.cloudPojos != null){
            if (o1.cloudPojos != null){
                if (!this.cloudPojos.equals(o1.cloudPojos)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.cloudPojos != null){
                return false;
            }
        }


        if (this.currentPlayerPojo != null){
            if (o1.currentPlayerPojo != null){
                if (!this.currentPlayerPojo.equals(o1.currentPlayerPojo)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.currentPlayerPojo != null){
                return false;
            }
        }

        if (this.playerPojos != null){
            if (o1.playerPojos != null){
                if (!this.playerPojos.equals(o1.playerPojos)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.playerPojos != null){
                return false;
            }
        }

        boolean found = false;
        for (CharacterPojo o1c : o1.characterPojos){
            found = false;
            for (CharacterPojo thisc : this.characterPojos){
                if (o1c.equals(thisc)){
                    found = true;
                }
            }
            if (found = false){
                return false;
            }
        }
        for (CharacterPojo thisc : this.characterPojos){
            found = false;
            for (CharacterPojo o1c : o1.characterPojos){
                if (o1c.equals(thisc)){
                    found = true;
                }
            }
            if (found = false){
                return false;
            }
        }

        return true;
    }
}

package it.polimi.ingsw;

import java.util.*;

public class Controller {

    private static Controller instance;
    private List<Game> gameSavings;
    private Game game;
    private GamePhase currentPhase;
    private Player firstPianificationPlayer;

    private Controller(){
        this.instance=null;
    }

    public static Controller getIntance(){
        if (instance == null){
            instance = new Controller();
        }
        return instance;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Game> getGameSavings() {
        return gameSavings;
    }

    public void saveGame(String savingName){
        this.game.setGameId(savingName);
        this.gameSavings.add(this.game);
    }

    public Player getFirstPianificationPlayer() {
        return firstPianificationPlayer;
    }

    public void setFirstPianificationPlayer(Player firstPianificationPlayer) {
        this.firstPianificationPlayer = firstPianificationPlayer;
    }
}

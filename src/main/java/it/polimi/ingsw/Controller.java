package it.polimi.ingsw;

import java.util.*;

public class Controller {

    private static Controller instance = null;
    private List<Game> gameSavings;
    private Game game;

    private Controller(){};

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


}

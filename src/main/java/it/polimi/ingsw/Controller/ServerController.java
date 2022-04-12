package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.*;

public class ServerController {

    ///////////////////////////////////////// THREAD POOL //////////////////////////////////
    private static ServerController instance;
    private Map<Integer, Game> gameSavings;

    private ServerController(){
        this.instance=null;
    }

    public static ServerController getInstance(){
        if (instance == null){
            instance = new ServerController();
        }
        return instance;
    }

    public Map<Integer, Game> getGameSavings() {
        return gameSavings;
    }

    public void setGameSaving(Game gameSaving) {
        this.gameSavings.put(gameSaving.getGameId(), gameSaving);
    }

    public void saveGame(Game gameToSave){
        this.gameSavings.put(gameToSave.getGameId(), gameToSave );
    }


}

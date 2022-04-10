package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.*;

public class ServerController {

    ///////////////////////////////////////// THREAD POOL //////////////////////////////////
    private static ServerController instance;
    private Map<Integer, Game> gameSavings;
    private List<GameController> gameControllers;
        //ATTENZIONE: lastId è per avere un random
    private Integer lastId;


    private ServerController(){
        this.instance=null;
    }

    public static ServerController getIntance(){
        if (instance == null){
            instance = new ServerController();
        }
        return instance;
    }



    public List<GameController> getGameControllers() {
        return gameControllers;
    }

    public void addGameController(GameMode mode, Lobby lobby) {
        Integer gameId;

        gameId=lastId+1;
        lastId=lastId+1;
        GameController newGameController;
        if(mode==GameMode.Complex_2 || mode==GameMode.Complex_3){
            newGameController= new GameController(lobby, gameId, true);
        }
        else{
            newGameController= new GameController(lobby, gameId, false);
        }

        this.gameControllers.add(newGameController);
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

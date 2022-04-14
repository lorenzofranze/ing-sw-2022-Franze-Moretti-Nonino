package it.polimi.ingsw.server.Controller;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {

    ///////////////////////////////////////// THREAD POOL //////////////////////////////////
    private static ServerController instance;
    private ExecutorService executorService;

    public Map<Integer, GameController> getCurrentGames() {
        return currentGames;
    }

    private Map<Integer, GameController> currentGames;
    private Lobby toStart=null;

    private ServerController(){
        this.instance=null;
        executorService= Executors.newCachedThreadPool();
    }

    public static ServerController getInstance(){
        if (instance == null){
            instance = new ServerController();
        }
        return instance;
    }

    public void play(){
        LobbyManager lobbyManager = new LobbyManager(50000);
        Thread t1 = new Thread(lobbyManager);
        t1.start();
        while(true){
            if(toStart!=null){
                GameController gameController = new GameController(toStart, toStart.getGameMode().isExpert());
                currentGames.put(gameController.getGameID(), gameController);
                executorService.submit(gameController);
                toStart=null;
            }
        }
        //executorService.shutdown();

    }

    public void setToStart(Lobby toStart){
        this.toStart = toStart;
    }

    public void removeCurrentGame(int gameID){
        this.currentGames.remove(gameID);
    }


}

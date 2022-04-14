package it.polimi.ingsw.server.Controller;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {

    ///////////////////////////////////////// THREAD POOL //////////////////////////////////
    private static ServerController instance;
    private ExecutorService executorService;



    private Map<Integer, GameController> currentGames;
    private Lobby toStart=null;
    private Integer toStop=null;

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

    public void play() throws InterruptedException{
        LobbyManager lobbyManager = new LobbyManager(50000);
        Thread t1 = new Thread(lobbyManager);
        t1.start();
        while(true){
            while(toStart==null && toStop==null ){
                wait();
            }
            if(toStart!=null){
                GameController gameController = new GameController(toStart, toStart.getGameMode().isExpert());
                currentGames.put(gameController.getGameID(), gameController);
                executorService.submit(gameController);
                setToStart(null);
            }else if(toStop!=null){
                removeCurrentGame(toStop);
                setToStop(null);
            }
        }
        //executorService.shutdown();

    }


    public void setToStart(Lobby toStart){
        synchronized (toStart) {
            this.toStart = toStart;
        }
    }
    public void setToStop(Integer toStop){
        synchronized (toStop) {
            this.toStop = toStop;
        }
    }

    public void removeCurrentGame(int gameID){
        this.currentGames.remove(gameID);
    }

    public Map<Integer, GameController> getCurrentGames() {
        return currentGames;
    }


}

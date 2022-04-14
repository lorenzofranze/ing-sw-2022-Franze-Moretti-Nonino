package it.polimi.ingsw.server.Controller.Network;

public class StopManager implements Runnable{
    private static StopManager instance= null;

    private StopManager(){

    }
    public static StopManager getInstance() {
        if (instance == null) {
            instance = new StopManager();
        }
        return instance;
    }

    public void run(){

    }
}

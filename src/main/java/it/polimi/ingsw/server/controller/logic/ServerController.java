package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.LobbyManager;
import it.polimi.ingsw.server.model.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {

    ///////////////////////////////////////// THREAD POOL //////////////////////////////////
    private static ServerController instance;
    private ExecutorService executorService;

    private Map<Integer, GameController> currentGames;
    private Lobby toStart;
    private Integer toStop;

    private ServerController(){
        this.instance=null;
        this.currentGames=new HashMap<>();
        executorService= Executors.newCachedThreadPool();
    }

    public static ServerController getInstance(){
        if (instance == null){
            instance = new ServerController();
        }
        return instance;
    }



    /* ATTENZIONE:
    *  executorService non va bene perchè qualunque cosa facciamo per cancellare un gameController ad esempio
    * aggiorniamo un attributo o altro essendo 2 thread separati è possibile che ancora il metodo di gameController
    * sia in esecuzione -> problema di concorrenza, serve qualche altra collezione di Thread che permette di fare
    *  join() per aspettare la terminazione e poi cancellare
     */

    public void play() {
        // thread that starts games
        LobbyManager lobbyManager = LobbyManager.getInstance();
        //Thread t1 = new Thread(lobbyManager); //todo l'ho tolto
        lobbyManager.getStarted();
    }


    public void start(Lobby toStart){
        this.toStart = toStart;
        GameController gameController = new GameController(toStart, toStart.getGameMode().isExpert());
        currentGames.put(gameController.getGameID(), gameController);
        executorService.submit(gameController);
    }

    public synchronized void setToStop(Integer toStop){
            this.currentGames.remove(toStop);
    }

    public Map<Integer, GameController> getCurrentGames() {
        return currentGames;
    }

    /** closes the sokets of all the players of a game
     * when a player quit the game with a disconnection message
     * @param playerNickname
     */
    public void closeConnection(String playerNickname) {
        System.out.println("Connection closed from the one of the players");

        //find the lobby that hosts the player who has disconnected from the game

        Lobby lobby=null;
        for (GameController gameController : this.getInstance().getCurrentGames().values()) {
            for (Player p : gameController.getGame().getPlayers()) {
                if(p.getNickname().equals(playerNickname)){
                    lobby=gameController.getLobby();
                    ServerController.getInstance().getCurrentGames().remove(gameController);
                }
            }
        }
        try {
            for (Socket socket : lobby.getUsersReadyToPlay().values())
                socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        setToStop(1);
    }


}

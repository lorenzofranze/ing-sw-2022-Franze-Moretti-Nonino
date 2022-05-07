package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.DisconnectionMessage;
import it.polimi.ingsw.server.controller.logic.GameController;
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

    public void play() {
        // thread that starts games
        LobbyManager lobbyManager = LobbyManager.getInstance();
        try {
            lobbyManager.welcomeNewPlayers();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        MessageHandler messageHandler=null;
        for (GameController gameController : this.getInstance().getCurrentGames().values()) {
            for (Player p : gameController.getGame().getPlayers()) {
                if(p.getNickname().equals(playerNickname)){
                    lobby=gameController.getLobby();
                    messageHandler=gameController.getMessageHandler();
                    ServerController.getInstance().getCurrentGames().remove(gameController);
                }
            }
        }
        try {
            //avvisa gli utenti che il gioco è finito per colpa di una disconnessione
            for (PlayerManager playerManager: messageHandler.getPlayerManagerMap().values()){
                DisconnectionMessage disconnectionMessage=new DisconnectionMessage();
                playerManager.sendMessage(disconnectionMessage);
            }
            for (Socket socket : lobby.getUsersReadyToPlay().values()){
                socket.close();
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


    }


}

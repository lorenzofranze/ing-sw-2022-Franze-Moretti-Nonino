package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.AsyncMessage;
import it.polimi.ingsw.common.messages.ErrorMessage;
import it.polimi.ingsw.common.messages.TypeOfError;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.common.messages.AsyncMessage;

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
    private Lobby lobbyToStart;

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

    public void turnOn() {
        // thread that starts games
        LobbyManager lobbyManager = LobbyManager.getInstance();
        try {
            lobbyManager.welcomeNewPlayers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startGame(Lobby lobbyToStart){
        this.lobbyToStart = lobbyToStart;
        GameController gameController = new GameController(lobbyToStart, lobbyToStart.getGameMode().isExpert());
        currentGames.put(gameController.getGameID(), gameController);
        executorService.submit(gameController);
    }

    public synchronized void setToStop(Integer toStop){
        this.currentGames.get(toStop).setGameOver(true);
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
        GameController gameControllerToStop=null;
        for (GameController gameController : this.getInstance().getCurrentGames().values()) {
            for (Player p : gameController.getGame().getPlayers()) {
                if(p.getNickname().equals(playerNickname)){
                    lobby=gameController.getLobby();
                    messageHandler=gameController.getMessageHandler();
                    gameControllerToStop=gameController;
                }
            }
        }
        try {
            //avvisa gli utenti che il gioco è finito per colpa di una disconnessione
            AsyncMessage asyncMessage=new AsyncMessage();
            for (PlayerManager playerManager: messageHandler.getPlayerManagerMap().values()){
                if(!playerManager.getPlayerNickname().equals(playerNickname)) {
                    playerManager.sendMessage(asyncMessage);
                }
            }



            for (Socket socket : lobby.getUsersReadyToPlay().values()){
                socket.close();
            }

            ServerController.getInstance().getCurrentGames().remove(gameControllerToStop);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}



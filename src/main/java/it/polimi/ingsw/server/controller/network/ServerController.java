package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.AsyncMessage;
import it.polimi.ingsw.common.messages.ErrorMessage;
import it.polimi.ingsw.common.messages.TypeOfError;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.common.messages.AsyncMessage;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystemNotFoundException;
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
        System.out.println("ServerController closeConnection: player "+ playerNickname + " is disconnected");

        //find the lobby that hosts the player who has disconnected from the game

        Lobby lobby=null;
        MessageHandler messageHandler=null;
        GameController gameControllerToStop=null;
        for (GameController gameController : this.getInstance().getCurrentGames().values()) {
            for (Player p : gameController.getGame().getPlayers()) {
                if(p.getNickname().equals(playerNickname)){
                    lobby=gameController.getLobby();
                    gameControllerToStop=gameController;
                    messageHandler=gameControllerToStop.getMessageHandler();
                }
            }
        }
        if(messageHandler!=null) {
            //avvisa gli utenti che il gioco è finito per colpa di una disconnessione
            AsyncMessage asyncMessage = new AsyncMessage();
            for (PlayerManager playerManager : messageHandler.getPlayerManagerMap().values()) {
                if (!playerManager.getPlayerNickname().equals(playerNickname)) {
                    playerManager.sendMessage(asyncMessage);
                    System.out.println("avviso interruzione gioco");
                    if (playerManager.getPingThread().isInterrupted() == false) {
                        playerManager.getPingThread().interrupt();
                    }
                /*
                if(messageHandler.getPlayerManagerThreads().get(playerManager.getPlayerNickname()).isInterrupted()==false){
                    messageHandler.getPlayerManagerThreads().get(playerManager.getPlayerNickname()).interrupt();
                }
                */
                }
            }
        }
        if(gameControllerToStop!=null){
            if(this.currentGames.keySet().contains(gameControllerToStop.getGameID())){
                setToStop(gameControllerToStop.getGameID());
            }

            gameControllerToStop.setForceStop(true);
        }


        if(lobby!=null){
            for (Socket socket : lobby.getUsersReadyToPlay().values()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}




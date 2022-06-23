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

    /**
     * There is only one lobby manager.
     * The main creates the ServerController and calls turnOn method so that the lobby manager is created immediatly
     * when the server starts running.
     */
    public void turnOn(int lobbyPortNumber) {
        // thread that starts games
        LobbyManager lobbyManager = LobbyManager.getInstance(lobbyPortNumber);
        try {
            lobbyManager.welcomeNewPlayers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * When a lobby is full, the game is put in the currentGames list and the game starts
     * @param lobbyToStart
     */
    public void startGame(Lobby lobbyToStart){
        this.lobbyToStart = lobbyToStart;
        GameController gameController = new GameController(lobbyToStart, lobbyToStart.getGameMode().isExpert());
        currentGames.put(gameController.getGameID(), gameController);
        executorService.submit(gameController);
    }

    public synchronized void setToStop(Integer toStop){
        this.currentGames.remove(toStop);
    }

    public Map<Integer, GameController> getCurrentGames() {
        return currentGames;
    }

    /**
     * Closes the sokets of all the players of a game
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
                    System.out.println("avviso interruzione gioco");
                    if (playerManager.getPingThread().isInterrupted() == false) {
                        playerManager.getPingThread().interrupt();
                    }
                    playerManager.sendMessage(asyncMessage);
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
                gameControllerToStop.setGameOver(true);
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
        System.out.println("\n La partita dei giocatore "+ playerNickname+ " è finita");
    }
}




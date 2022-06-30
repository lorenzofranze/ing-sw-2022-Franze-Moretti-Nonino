package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.persistence.Saving;
import it.polimi.ingsw.server.controller.persistence.SavingsMenu;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.common.messages.AsyncMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * There is just one instance of ServerController and it is created by ServerApp-class.
 * ServerApp calls turnOn: a lobbyManager (unique) is created and begins to welcome players to the game.
 * When a lobby is full, a new game controller is created and the game is put in the currentGames list
 * and the game starts.
 * It manages the closure of connection:
 * - firstly, finds the lobby that hosts the player who has disconnected from the game (or the waiting lobby of the player)
 * - secondly, notifies all the other player about the imminent end of the connection
 * - then, removes the game of the list of current-games (or the player from the waiting-lobby), sets game over to true and
 *   closes the sokets of all the players of a game
 */
public class ServerController {

    ///////////////////////////////////////// THREAD POOL //////////////////////////////////
    private static ServerController instance;
    private ExecutorService executorService;

    private Map<Integer, GameController> currentGames;
    private Lobby lobbyToStart;
    private SavingsMenu savingsMenu;

    /**creates a ServerController.
     * If there is a savingMenu.txt file, the new ServerController gains the information
     * contained in that file (= SavingMenu and gameControllerID);
     * If a savingMenu.txt file doesn't exists, it creats a new empty savingMenu.txt file*/
    private ServerController(){
        this.instance=null;
        this.currentGames=new HashMap<>();
        executorService= Executors.newCachedThreadPool();
        savingsMenu = new SavingsMenu();

        String data = new String();
        try {
            String currentPath = new File(".").getCanonicalPath();
            String fileName = currentPath + "/savings/SavingsMenu.txt";
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = data + myReader.nextLine();
            }
            myReader.close();

            this.savingsMenu = JsonConverter.fromJsonToSavingsMenu(data);

        } catch (FileNotFoundException e) {
            //it means that it is the first time that the Server has been created, there are no savings.
            //Creation of a file containing savingsMenu

            try {
                String currentPath = new File(".").getCanonicalPath();
                String fileName = currentPath + "/savings";
                File file = new File(fileName);
                file.mkdirs();
            }catch (IOException ex){
                e.printStackTrace();
            }

            try {
                String currentPath = new File(".").getCanonicalPath();
                String fileName = currentPath + "/savings/SavingsMenu.txt";
                File file = new File(fileName);
                file.createNewFile();
                String dataString = JsonConverter.fromSavingsMenuToJson(this.savingsMenu);
                FileOutputStream outputStream = new FileOutputStream(file, false);
                byte[] strToBytes = dataString.getBytes();
                outputStream.write(strToBytes);
                outputStream.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
     * When a lobby is full, a new game controller is created and the game is put in the currentGames list
     * and the game starts
     * @param lobbyToStart
     */
    public void startGame(Lobby lobbyToStart){
        this.lobbyToStart = lobbyToStart;
        if (savingsMenu.getSavingFromNicknames(lobbyToStart.getUsersNicknames()) == null){
            GameController gameController = new GameController(lobbyToStart, lobbyToStart.getGameMode().isExpert());
            currentGames.put(gameController.getGameID(), gameController);
            executorService.submit(gameController);
        }else{
            Saving saving = savingsMenu.getSavingFromNicknames(lobbyToStart.getUsersNicknames());
            GameController gameController = new GameController(lobbyToStart, saving);
            currentGames.put(gameController.getGame().getGameId(), gameController);
            executorService.submit(gameController);
        }
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
     * It manages the closure of connection:
     * - firstly, finds the lobby that hosts the player who has disconnected from the game (or the waiting lobby of the player)
     * - secondly, notifies all the other player about the imminent end of the connection
     * - then, removes the game of the list of current-games (or the player from the waiting-lobby) and
     *   closes the sokets of all the players of a game
     * @param playerNickname
     */
    public void closeConnection(String playerNickname) {
        System.out.println("connection player "+ playerNickname + " closed");

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
        //if lobby==null: it is in a waiting-lobby
        if(lobby==null){
            LobbyManager lobbyManager=LobbyManager.getInstance();
            for( Lobby waitingLobby: lobbyManager.getWaitingLobbies().values()){
                if(waitingLobby.getUsersNicknames().contains(playerNickname)){
                    waitingLobby.removeDisconnectedPlayer(playerNickname);
                }
            }

        }
        if(messageHandler!=null) {
            //avvisa gli utenti che il gioco è finito per colpa di una disconnessione
            AsyncMessage asyncMessage = new AsyncMessage();
            for (PlayerManager playerManager : messageHandler.getPlayerManagerMap().values()) {
                if (!playerManager.getPlayerNickname().equals(playerNickname)) {
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
    }

    public SavingsMenu getSavingsMenu() {
        return savingsMenu;
    }
}




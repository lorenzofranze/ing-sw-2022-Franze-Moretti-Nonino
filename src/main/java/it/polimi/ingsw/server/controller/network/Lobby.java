package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.logic.GameMode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
    // string è nickname
    private Map<String, Socket> usersReadyToPlaySocket;
    private Map<String, PlayerManager> usersPlayerManager;
    private Map<PlayerManager,Thread> playerManagerThreads;

    public GameMode getGameMode() {
        return gameMode;
    }

    private GameMode gameMode;

    public Map<String, Socket> getUsersReadyToPlay() {
        return usersReadyToPlaySocket;
    }

    public Lobby(GameMode gameMode) {
        this.usersReadyToPlaySocket = new HashMap<>();
        this.usersPlayerManager = new HashMap<>();
        this.playerManagerThreads= new HashMap<>();
        this.gameMode = gameMode;

    }

    public List<String> getUsersNicknames() {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.addAll(usersReadyToPlaySocket.keySet());
        return nicknames;
    }

    /**
     * When a player chooses the nickname and the lobby, he is added to the lobby.
     * The lobby associate him with a playerManager (and with a playerManagerThread).
     * The playerManagerThread starts running.
     * @param nickname
     * @param clientSocket
     * @param playerManager
     */
    public void addUsersReadyToPlay(String nickname, Socket clientSocket, PlayerManager playerManager) {
        usersReadyToPlaySocket.put(nickname, clientSocket);
        usersPlayerManager.put(nickname, playerManager);
        Thread playerManagerThread = new Thread(playerManager);
        playerManagerThread.start();
        playerManagerThreads.put(playerManager,playerManagerThread);
        if(playerManager.isToStop()==true){
            playerManagerThread.interrupt();
        }
    }

    /**
     * If a player disconnect while waiting in the lobby,
     * he must be removed from the (uncomplete)-lobby
     * @param playerNickname
     */
    public void removeDisconnectedPlayer(String playerNickname){
        usersReadyToPlaySocket.remove(playerNickname);
        playerManagerThreads.remove(usersPlayerManager.get(playerNickname));
        usersPlayerManager.remove(playerNickname);
    }

    public Map<String, PlayerManager> getUsersPlayerManager() {
        return usersPlayerManager;
    }

    public Map<String, Socket> getUsersReadyToPlaySocket() {
        return usersReadyToPlaySocket;
    }

    public Map<PlayerManager, Thread> getPlayerManagerThreads() {
        return playerManagerThreads;
    }
}


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
        this.gameMode = gameMode;
    }

    public List<String> getUsersNicknames() {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.addAll(usersReadyToPlaySocket.keySet());
        return nicknames;
    }

    public void addUsersReadyToPlay(String nickname, Socket clientSocket, PlayerManager playerManager) {
        usersReadyToPlaySocket.put(nickname, clientSocket);
        usersPlayerManager.put(nickname, playerManager);
    }

    public Map<String, PlayerManager> getUsersPlayerManager() {
        return usersPlayerManager;
    }

    public Map<String, Socket> getUsersReadyToPlaySocket() {
        return usersReadyToPlaySocket;
    }
}


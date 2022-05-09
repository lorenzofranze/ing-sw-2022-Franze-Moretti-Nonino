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
    private Map<String, BufferedReader> usersReadyToPlayBufferedReader;
    private Map<String, BufferedWriter> usersReadyToPlayBufferedWriter;

    public GameMode getGameMode() {
        return gameMode;
    }

    private GameMode gameMode;

    public Map<String, Socket> getUsersReadyToPlay() {
        return usersReadyToPlaySocket;
    }

    public Lobby(GameMode gameMode){
        this.usersReadyToPlaySocket = new HashMap<>();
        this.usersReadyToPlayBufferedWriter = new HashMap<>();
        this.usersReadyToPlayBufferedReader = new HashMap<>();
        this.gameMode = gameMode;
    }

    public List<String> getUsersNicknames() {
        ArrayList<String> nicknames= new ArrayList<>();
        nicknames.addAll(usersReadyToPlaySocket.keySet());
        return nicknames;
    }

    public void addUsersReadyToPlay(String nickname, Socket clientSocket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        usersReadyToPlaySocket.put(nickname,clientSocket);
        usersReadyToPlayBufferedReader.put(nickname, bufferedReader);
        usersReadyToPlayBufferedWriter.put(nickname, bufferedWriter);
    }

    public Map<String, BufferedReader> getUsersReadyToPlayBufferedReader() {
        return usersReadyToPlayBufferedReader;
    }

    public Map<String, BufferedWriter> getUsersReadyToPlayBufferedWriter() {
        return usersReadyToPlayBufferedWriter;
    }
}

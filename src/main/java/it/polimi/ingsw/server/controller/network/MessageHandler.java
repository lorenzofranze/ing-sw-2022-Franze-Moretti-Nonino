package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class MessageHandler {
    Scanner scanner = new Scanner(System.in);
    public int value;
    private int index =0;

    private int portNumber;
    private Map<String,PlayerManager> playerManagerMap;
    private Map<PlayerManager,Thread> playerManagerThreads;

    private JsonConverter jsonConverter;


    /**
     * There is one MessageHandler for each game. The PlayerManagerMap associates each player with his
     * personal playerManager
     * @param lobby
     */
    public MessageHandler(Lobby lobby){
        playerManagerThreads= new HashMap<>();
        Thread t;
        playerManagerMap=lobby.getUsersPlayerManager();
        //inetAddresses= new HashMap<>();
        playerManagerThreads=lobby.getPlayerManagerThreads();
        jsonConverter = new JsonConverter();
    }


    public PlayerManager getPlayerManager(String nickname) {
        return playerManagerMap.get(nickname);
    }

    //restituisce il valore inserito dal giocatore player
    //in qualche modo chiedo il valore al giocatore e lo restituisco
    public int getValue(Player player) {
        return value;
    }


    public Map<String, PlayerManager> getPlayerManagerMap() {
        return playerManagerMap;
    }


    /**
     * Reads from the buffer and returns the corresponding string
     * @param nickname
     * @return
     */
    private String readFromBuffer(String nickname){
        BufferedReader in = playerManagerMap.get(nickname).getBufferedReaderIn();
        String lastMessage = "";

        try{
            String line = in.readLine();
            while (!line.equals("EOF")){
                lastMessage = lastMessage + line + "\n";
                line = in.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return lastMessage;
    }

    /**
     * Sends a message to all the players
     * @param message
     */
    public void sendBroadcast(Message message){
        for(PlayerManager playerManager : this.getPlayerManagerMap().values()){
            playerManager.sendMessage(message);
        }
    }

    /**
     * For all the player managers:
     * it sets isMyTurn=false for all players exept the current player
     * it sets isMyTurn=true for the current player
     * @param currPlayer
     */
    public void setTurn(String currPlayer){
        for(PlayerManager playerManager: playerManagerMap.values()){
            if(playerManager.getPlayerNickname().equals(currPlayer)){
                playerManager.setMyTurn(true);
            }
            else playerManager.setMyTurn(false);
        }
    }

    public void setPlayerManagerMap(Map<String, PlayerManager> playerManagerMap) {
        this.playerManagerMap = playerManagerMap;
    }

    public Map<PlayerManager, Thread> getPlayerManagerThreads() {
        return playerManagerThreads;
    }
}

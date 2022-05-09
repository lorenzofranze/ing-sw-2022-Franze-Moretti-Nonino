package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MessageHandler {
    Scanner scanner = new Scanner(System.in);
    public int value;
    private int index =0;
    private int[] buffer = {1,2,3}; // inserire valori da provare con getValueTest()

    private int portNumber;
    private Map<String,BufferedReader> bufferedReaderIn;
    private Map<String,BufferedWriter> bufferedReaderOut;
    private Map<String,PlayerManager> playerManagerMap;

    private JsonConverter jsonConverter;


    public MessageHandler(Lobby lobby){

        bufferedReaderIn= new HashMap<>();
        bufferedReaderOut= new HashMap<>();
        playerManagerMap=new HashMap<>();
        //inetAddresses= new HashMap<>();
        for(String s: lobby.getUsersReadyToPlay().keySet()){
            PlayerManager playerManager;
            Socket clientSocket = lobby.getUsersReadyToPlay().get(s);
            if (clientSocket==null) return;
            try{
                BufferedReader in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                bufferedReaderIn.put(s, in);
                BufferedWriter out= new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                bufferedReaderOut.put(s, out);
                playerManager=new PlayerManager(s, bufferedReaderIn.get(s), bufferedReaderOut.get(s));
                playerManagerMap.put(s,playerManager);
                Thread t = new Thread(playerManager);
                t.start();
                if(playerManager.isToStop()==true){
                    t.interrupt();
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
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


    public void setWinner(Player winner){
        System.out.println(winner);
    }

    public Map<String, PlayerManager> getPlayerManagerMap() {
        return playerManagerMap;
    }

    /*
    /** parses from message-type to json string, then sends to the player the message, reads the answer from the player,
     * parses from json string to message-type
     * @param gameController
     * @param messageToSend
     * @return answer-message to the game controller. The game controller has to verify the validity of the answer.
     * @throws IOException
     */
    /*
    public Message communicationWithClient(ServerMessage messageToSend){
       String stringToSend = jsonConverter.fromMessageToJson(messageToSend);
       String nickname= messageToSend.getNickname();
       String receivedString=null;
       ClientMessage receivedMessage=null;

        boolean isValid= false;
        while(!isValid){
            try {
                bufferedReaderOut.get(nickname).write(stringToSend);
                bufferedReaderOut.get(nickname).flush();
                receivedString= readFromBuffer(nickname);
            } catch (IOException e) {
                e.printStackTrace();
            }
            receivedMessage = (ClientMessage) jsonConverter.fromJsonToMessage(receivedString);
        }
       return receivedMessage;
    }


    public void sendUpdate(GameController gameController, ServerMessage messageToSend){
        String stringToSend = jsonConverter.fromMessageToJson(messageToSend);
        String nickname= messageToSend.getNickname();
        String receivedString=null;
        ClientMessage receivedMessage=null;

        boolean isValid= false;

        try {
            bufferedReaderOut.get(nickname).write(stringToSend);
            bufferedReaderOut.get(nickname).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */


    public Map<String, BufferedReader> getBufferedReaderIn() {
        return bufferedReaderIn;
    }
    public Map<String, BufferedWriter> getBufferedReaderOut() {
        return bufferedReaderOut;
    }

    private String readFromBuffer(String nickname){
        BufferedReader in = bufferedReaderIn.get(nickname);
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

    public void sendBroadcast(Message message){
        for(PlayerManager playerManager : this.getPlayerManagerMap().values()){
            playerManager.sendMessage(message);
            System.out.println("ACK FULL LOBBY SENT TO: " + playerManager.getPlayerNickname());
        }
    }

    /**
     * for all the player managers:
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
}

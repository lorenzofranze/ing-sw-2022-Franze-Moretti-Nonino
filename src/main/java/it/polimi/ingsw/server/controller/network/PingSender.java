
package it.polimi.ingsw.Server.Controller.Network;



import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.PingMessage;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.network.ServerController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class PingSender implements Runnable{
    private boolean isConnected;
    //1 minute ping timeout
    private final static int PING_TIMEOUT= 60000;

    private BufferedWriter bufferedWriterOut;
    private String playerNickname;

    public PingSender(String playerNickname, BufferedWriter bufferedReaderOut){
        this.playerNickname=playerNickname;
        this.bufferedWriterOut=bufferedReaderOut;

        isConnected=true;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public void run() {
        while(isConnected){
            PingMessage message= new PingMessage();
            JsonConverter jsonConverter=new JsonConverter();
            String messageString= jsonConverter.fromMessageToJson(message);
            this.isConnected=false;


            //invio il ping
            sendPingMessage();

            try {
                Thread.sleep(PING_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // se arriva il pong, player manager setta isconnected a true e continua il while
        }

        //RESILIENZA ALLE DISCONNESSIONI
        /*
        LobbyManager lobbyManager=LobbyManager.getInstance();
        lobbyManager.addDisconnectedPlayers(nickname);
        */


        //SE ARRIVO QUI è DISCONNESSO
        ServerController.getInstance().closeConnection(playerNickname);

    }

    public void sendPingMessage(){

        JsonConverter jsonConverter= new JsonConverter();
        String stringToSend = jsonConverter.fromMessageToJson(new PingMessage());

        try {
            bufferedWriterOut.write(stringToSend);
            bufferedWriterOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


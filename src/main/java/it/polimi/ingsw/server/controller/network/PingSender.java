
package it.polimi.ingsw.Server.Controller.Network;



import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.PingMessage;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.network.LobbyManager;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.controller.network.ServerController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class PingSender implements Runnable{
    private volatile boolean isConnected;
    //1 minute ping timeout
    private final static int PING_TIMEOUT= 60000;
    private final static int RECONNECTION_TIMEOUT= 20000;


    private PlayerManager playerManager;
    private String playerNickname;

    public PingSender(String playerNickname, PlayerManager playerManager){
        this.playerNickname=playerNickname;
        this.playerManager=playerManager;

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
        try {
            Thread.sleep(PING_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(isConnected) {
            PingMessage message = new PingMessage();
            JsonConverter jsonConverter = new JsonConverter();
            String messageString = jsonConverter.fromMessageToJson(message);
            this.isConnected = false;


            //invio il ping
            playerManager.sendMessage(new PingMessage());

            try {
                Thread.sleep(PING_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (this.isConnected = false) {
                System.out.println("Il ping del client " + playerNickname + "non è più arrivato al server");

                //RESILIENZA ALLE DISCONNESSIONI
                LobbyManager lobbyManager = LobbyManager.getInstance();
                lobbyManager.addDisconnectedPlayers(playerNickname);
                try {
                    Thread.sleep(RECONNECTION_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
            // se arriva il pong, player manager (o nel caso di resilienza, il lobby manager)
            // setta isconnected a true e continua il while

        //SE ARRIVO QUI è DISCONNESSO
        ServerController.getInstance().closeConnection(playerNickname);
    }
}


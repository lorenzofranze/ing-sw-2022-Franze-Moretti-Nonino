package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.PingMessage;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.controller.network.ServerController;

import java.io.IOException;

public class PingSenderFromClient implements Runnable {
    private volatile boolean isConnected;
    //1 minute ping timeout
    private final static int PING_TIMEOUT= 600000000;

    public PingSenderFromClient(){
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

        while(isConnected) {
            PingMessage message = new PingMessage();
            JsonConverter jsonConverter = new JsonConverter();
            String messageString = jsonConverter.fromMessageToJson(message);
            this.isConnected = false;


            //invio il ping
            try {
                ClientController.getInstance().getNetworkHandler().sendToServer(new PingMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(PING_TIMEOUT);
            } catch (InterruptedException e) {
                System.out.println("Interrompo il timeout del ping");
                ClientController.getInstance().getNetworkHandler().endClient();
            }

            if (this.isConnected = false) {
                System.out.println("Il ping del server non è più arrivato al client");
                break;
                /*
                //RESILIENZA ALLE DISCONNESSIONI

                LobbyManager lobbyManager = LobbyManager.getInstance();
                lobbyManager.addDisconnectedPlayers(playerNickname);
                try {
                    Thread.sleep(RECONNECTION_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */

            }
        }
        // se arriva il pong, player manager (o nel caso di resilienza, il lobby manager)
        // setta isconnected a true e continua il while

        //SE ARRIVO QUI è DISCONNESSO

    }
}

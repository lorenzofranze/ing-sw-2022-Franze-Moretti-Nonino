
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
    //1 minute ping timeout
    private final static int PING_TIMEOUT= 30000;
    private final static int RECONNECTION_TIMEOUT= 20000;


    private PlayerManager playerManager;
    private String playerNickname;

    public PingSender(String playerNickname, PlayerManager playerManager){
        this.playerNickname=playerNickname;
        this.playerManager=playerManager;
    }


    @Override
    public void run() {

        while(playerManager.getConnected()) {
            PingMessage message = new PingMessage();
            JsonConverter jsonConverter = new JsonConverter();
            String messageString = jsonConverter.fromMessageToJson(message);
            playerManager.setConnected(false);


            //invio il ping
            playerManager.sendMessage(new PingMessage());

            try {
                Thread.sleep(PING_TIMEOUT);
            } catch (InterruptedException e) {
                System.out.println("Interrompo il timeout del ping perchè c'è stata un'eccezione");
                if(!playerManager.getCloseConnectionBeenCalled()) {
                    ServerController.getInstance().closeConnection(playerNickname);
                }
            }

            if (playerManager.getConnected() == false) {
                System.out.println("Il ping del client " + playerNickname + "non è più arrivato al server");
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
        System.out.println("Ping sender: is connected non è più true");
        if(!playerManager.getCloseConnectionBeenCalled()) {
            ServerController.getInstance().closeConnection(playerNickname);
        }
    }
}


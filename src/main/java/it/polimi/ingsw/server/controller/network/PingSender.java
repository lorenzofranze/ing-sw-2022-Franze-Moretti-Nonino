
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
    private final static int PING_TIMEOUT= 10000;
    private final static int RECONNECTION_TIMEOUT= 20000;


    private PlayerManager playerManager;
    private String playerNickname;

    public PingSender(PlayerManager playerManager){
        this.playerNickname=" invalidNickname ";
        this.playerManager=playerManager;
    }


    /**
     * While the player is connected, it sends a ping message to the player and waits for PING_TIMEOUT:
     * if, in the meantime, the playerManager.getConnected() has been set to true,
     * it goes on repetitevly,
     * otherwise it stops and closes the connection of that player (if not already closed in other part of the code)
     */
    @Override
    public void run() {

        //ping not used in tests
        if (playerManager.getBufferedReaderOut()==null){return;}

        while(playerManager.getConnected() && playerManager.getCloseConnectionBeenCalled()==false) {
            PingMessage message = new PingMessage();
            JsonConverter jsonConverter = new JsonConverter();
            String messageString = jsonConverter.fromMessageToJson(message);
            playerManager.setConnected(false);


            //invio il ping
            playerManager.sendMessage(new PingMessage());
            System.out.println("invio ping");

            try {
                Thread.sleep(PING_TIMEOUT);
            } catch (InterruptedException e) {
                System.out.println("Interrompo il timeout del ping perchè c'è stata un'eccezione");
                if(!playerManager.getCloseConnectionBeenCalled()) {
                    playerManager.setCloseConnectionBeenCalled(true);
                    ServerController.getInstance().closeConnection(playerNickname);
                }
            }

            if (playerManager.getConnected() == false) {
                System.out.println("Il ping del client " + playerNickname + " non è più arrivato al server");
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
            playerManager.setCloseConnectionBeenCalled(true);
            ServerController.getInstance().closeConnection(playerNickname);
        }
    }

    /**
     * Sets the nickname because during the connection the name is set to "invalid nickname" by default
     * @param playerNickname
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

}


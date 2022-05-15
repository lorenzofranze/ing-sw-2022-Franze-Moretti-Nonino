
package it.polimi.ingsw.Server.Controller.Network;



import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.PingMessage;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.controller.network.ServerController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class PingSender implements Runnable{
    private volatile boolean isConnected;
    //1 minute ping timeout
    private final static int PING_TIMEOUT= 60000;

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
        while(true){
            PingMessage message= new PingMessage();
            JsonConverter jsonConverter=new JsonConverter();
            String messageString= jsonConverter.fromMessageToJson(message);
            this.isConnected=false;


            //invio il ping
            playerManager.sendMessage(new PingMessage());

            try {
                Thread.sleep(PING_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(this.isConnected=false) {
                break;
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


}


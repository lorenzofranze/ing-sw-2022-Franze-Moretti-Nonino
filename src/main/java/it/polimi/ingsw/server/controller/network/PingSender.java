
package it.polimi.ingsw.Server.Controller.Network;



import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.PingMessage;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.network.ServerController;

public class PingSender implements Runnable{
    private boolean isConnected;
    //1 minute ping timeout
    private final static int PING_TIMEOUT= 60000;
    private String playerNickname;

    public PingSender(String playerNickname){
        this.playerNickname=playerNickname;
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
            message.setMessageType(TypeOfMessage.Ping);
            JsonConverter jsonConverter=new JsonConverter();
            String messageString= jsonConverter.fromMessageToJson(message);

            this.isConnected=false;


                //invio il ping
                /**todo**/



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
}



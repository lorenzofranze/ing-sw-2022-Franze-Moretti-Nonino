package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.PingMessage;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.controller.network.ServerController;

import java.io.IOException;

public class PingSenderFromClient implements Runnable {

    //1 minute ping timeout
    private final static int PING_TIMEOUT= 10000;
    NetworkHandler networkHandler;

    public PingSenderFromClient(NetworkHandler networkHandler){
        this.networkHandler=networkHandler;
    }


    /**
     * invia un messaggio di ping e attende PING_TIMEOUT secondi,
     * nel frattempo, networkHandler, se riceve un pong, chiamerà setPingConnected(true)
     * -- se ciò accade e non ci sono problemi di disconnessione si ripete tutto;
     * -- se non riceve il pong, interrompe il ciclo e chiama ClientController.setDisconnected();
     * -- se ci sono altri problemi di disconnessione, se non è già a true, chiama ClientController.setDisconnected()
     *    e ritorna.
     */
    @Override
    public void run() {

        while(networkHandler.isPingConnected()) {
            PingMessage message = new PingMessage();
            JsonConverter jsonConverter = new JsonConverter();
            String messageString = jsonConverter.fromMessageToJson(message);
            networkHandler.setPingConnected(false);


            //sending ping
            try {
                networkHandler.sendToServer(new PingMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(PING_TIMEOUT);
            } catch (InterruptedException e) {
                System.out.println("ping timeout interrupt");
                if(!ClientController.getInstance().isDisconnected()){
                    ClientController.getInstance().setDisconnected();
                }
                return;
            }

            if (networkHandler.isPingConnected() == false) {
                System.out.println("Ping form server not received");
                break;
            }
        }
        /*if pong is received, player manager (or lobby manager in case of resilience) sets isconnected=true and
        * the "while" continues*/
        if(!ClientController.getInstance().isDisconnected()){
            ClientController.getInstance().setDisconnected();
        }

        //here the client is disconnected

    }
}

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


            //invio il ping
            try {
                networkHandler.sendToServer(new PingMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(PING_TIMEOUT);
            } catch (InterruptedException e) {
                System.out.println("Interrompo il timeout del ping");
                if(!ClientController.getInstance().isDisconnected()){
                    ClientController.getInstance().setDisconnected();
                }
                return;
            }

            if (networkHandler.isPingConnected() == false) {
                System.out.println("Il ping del server non è più arrivato al client");
                break;
            }
        }
        // se arriva il pong, player manager (o nel caso di resilienza, il lobby manager)
        // setta isconnected a true e continua il while
        if(!ClientController.getInstance().isDisconnected()){
            ClientController.getInstance().setDisconnected();
        }

        //SE ARRIVO QUI è DISCONNESSO

    }
}

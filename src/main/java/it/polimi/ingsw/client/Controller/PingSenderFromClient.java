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
     * Sends a ping message and waits for PING_TIMEOUT seconds,
     *      * in the meanwhile, networkHandler, if receives a pong, will call setPingConnected(true)
     *      * -- if this happens and there are no disconnection problems it will repeat everything;
     *      * -- if it does not receive a pong, it stops the loop and calls ClientController.setDisconnected();
     *      * -- if there are other disconnection problems, if not already true, it calls ClientController.setDisconnected()
     *      * and returns.
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
                System.out.println("Ping from server not received");
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

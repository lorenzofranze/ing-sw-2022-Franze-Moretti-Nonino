package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.network.ServerController;

public class StartServer {
    public static void main(String Args[]){
        ServerController serverController = ServerController.getInstance();
        serverController.turnOn();
    }
}

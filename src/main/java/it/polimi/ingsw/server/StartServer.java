package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.logic.ServerController;

public class StartServer {
    public static void main(String Args[]){
        ServerController serverController = ServerController.getInstance();
        serverController.play();
    }
}

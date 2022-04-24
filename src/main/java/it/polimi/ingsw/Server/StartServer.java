package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Controller.ServerController;

public class StartServer {
    public static void main(String Args[]){
        ServerController serverController = ServerController.getInstance();
        serverController.play();
    }
}

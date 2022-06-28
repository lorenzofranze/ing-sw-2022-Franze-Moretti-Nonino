package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.network.ServerController;

import java.net.NetworkInterface;
import java.net.SocketException;

public class ServerApp {
    public static void main(String args[]) {
        ServerController serverController = ServerController.getInstance();
        int serverPort;
        serverPort = 32502;
        if (args.length == 0) {
            serverPort = 35002;
        } else {
            serverPort = Integer.parseInt(args[0]);
        }
        serverController.turnOn(serverPort);
    }
}

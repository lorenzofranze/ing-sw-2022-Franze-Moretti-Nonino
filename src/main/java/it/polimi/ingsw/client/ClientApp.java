package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.NetworkHandler;
import it.polimi.ingsw.client.View.*;
import it.polimi.ingsw.common.messages.ErrorMessage;
import it.polimi.ingsw.common.messages.TypeOfError;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class ClientApp {

    private static View view;

    private static String serverIp;
    private static int serverPort;

    public ClientApp(View view) {
        ClientApp.view = view;
    }

    public static void main(String[] args) {

        String typeOfView;
        View view;

        if (args.length == 0){
            serverIp = "localhost";
            serverPort = 32502;
            typeOfView = "cli";
        }else{
            typeOfView = args[0];
            serverIp = args[1];
            serverPort = Integer.parseInt(args[2]);
        }

        if(typeOfView.equals("cli")){
            view = new CLIView();
            ClientApp clientApp = new ClientApp(view);
            clientApp.start();
        }
        else if(typeOfView.equals("gui")){
            /*
            viewBegin = new GUIViewBegin();
            viewEnd = new GUIViewEnd();
            ClientApp clientApp = new ClientApp(viewBegin, viewEnd);
            clientApp.run();

             */
        }
    }


    public void start() {

        NetworkHandler networkHandler = new NetworkHandler(serverIp, serverPort);

        try {
            networkHandler.connectToServer();
        } catch (IOException e) {
            ErrorMessage errorMessage = new ErrorMessage(TypeOfError.FailedConnection);
            view.showMessage(errorMessage);
            return;
        }

        System.out.println("connesso al server"); //DA CANCELLARE

        ClientController clientController = ClientController.getInstance();
        clientController.setNetworkHandler(networkHandler);
        clientController.setView(view);

        Thread threadClientController= new Thread(clientController);
        threadClientController.start();

        /*
        ReentrantLock mutex = new ReentrantLock();
        mutex.lock();

         */
        Object clientControllerLock=clientController.getLock();
        synchronized (clientControllerLock){
            try {
                clientControllerLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            networkHandler.endClient();
            threadClientController.interrupt();
        }

    }


}
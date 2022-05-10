package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.NetworkHandler;
import it.polimi.ingsw.client.View.*;
import it.polimi.ingsw.common.messages.ErrorMessage;
import it.polimi.ingsw.common.messages.TypeOfError;

import java.io.IOException;

public class ClientApp implements Runnable{

    NetworkHandler networkHandler;
    ClientController clientController;

    private static ViewBegin viewBegin;
    private static ViewEnd viewEnd;

    private static String serverIp;
    private static int serverPort;

    public ClientApp(ViewBegin viewBegin, ViewEnd viewEnd) {
        ClientApp.viewBegin = viewBegin;
        ClientApp.viewEnd = viewEnd;
    }

    public static void main(String[] args) {

        String typeOfView;
        ViewBegin viewBegin;
        ViewEnd viewEnd;

        if (args.length == 0){
            serverIp = "localhost";
            serverPort = 32501;
            typeOfView = "cli";
        }else{
            typeOfView = args[0];
            serverIp = args[1];
            serverPort = Integer.parseInt(args[2]);
        }

        if(typeOfView.equals("cli")){
            viewBegin = new CLIViewBegin();
            viewEnd = new CLIViewEnd();
            ClientApp clientApp = new ClientApp(viewBegin, viewEnd);
            clientApp.run();
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


    @Override
    public void run() {

        networkHandler = new NetworkHandler(serverIp, serverPort);

        try {
            networkHandler.connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorMessage errorMessage = new ErrorMessage(TypeOfError.FailedConnection);
            viewBegin.showMessage(errorMessage);
        }

        System.out.println("connesso al server"); //DA CANCELLARE

        clientController = ClientController.getInstance();
        clientController.setNetworkHandler(networkHandler);
        clientController.setViewBegin(viewBegin);
        clientController.setViewEnd(viewEnd);

        clientController.run();

    }
}
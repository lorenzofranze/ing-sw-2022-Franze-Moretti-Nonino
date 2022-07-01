package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.NetworkHandler;
import it.polimi.ingsw.client.View.*;
import it.polimi.ingsw.client.View.GUI.GUIView;
import it.polimi.ingsw.client.View.GUI.GuiController;
import it.polimi.ingsw.common.messages.ErrorMessage;
import it.polimi.ingsw.common.messages.TypeOfError;

import java.io.IOException;

/**class where is located the Main to start the client*/
public class ClientApp{

    private static View view;

    private static String serverIp;
    private static int serverPort;

    public ClientApp(View view) {
        ClientApp.view = view;
    }

    /**
     * The game goes with cli or gui. It is called the method play()
     * @param args
     */
    public static void main(String[] args) {

        String typeOfView;
        View view;

        if (args.length == 0) {
            serverIp = "localhost";
            serverPort = 35002;
            typeOfView = "gui";
        } else {
            typeOfView = args[0];
            serverIp = args[1];
            serverPort = Integer.parseInt(args[2]);
        }

        if (typeOfView.equals("cli")) {
            view = new CLIView();
            ClientApp clientApp = new ClientApp(view);
            clientApp.play();
        } else if (typeOfView.equals("gui")) {
            view = new GUIView();
            ClientApp clientApp = new ClientApp(view);
            clientApp.play();
        }

        if(typeOfView.equals("cli")) {
            System.exit(0);
        }else if(typeOfView.equals("gui")){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().closeGame());
            GuiController.getInstance().runMethod();
        }
    }





    /**
     * Connection to the server is made with networkHandler.connectToServer();
     * a thread is created for running the clientController, this thread is interrupted
     * after clientController.setDisconnected() is called anywhere in the code:
     * in order to do this the clientControllerLock is an object on which it is called wait()
     * When in the method clientController.setDisconnected() will be called notifyAll(),
     * and the function can proceed with the interrupt() of the threadClientController.
     */
    public void play() {

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
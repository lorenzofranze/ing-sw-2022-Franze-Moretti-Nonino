package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.NetworkHandler;
import it.polimi.ingsw.client.View.*;
import it.polimi.ingsw.common.messages.ErrorMessage;
import it.polimi.ingsw.common.messages.TypeOfError;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class ClientApp{

    private static View view;

    private static String serverIp;
    private static int serverPort;

    public ClientApp(View view) {
        ClientApp.view = view;
    }

    /**
     * il gioco parte con cli o gui
     * viene chiamato il metodo play
     * @param args
     */
    public static void main(String[] args) {

        String typeOfView;
        View view;

        if (args.length == 0) {
            serverIp = "localhost";
            serverPort = 32502;
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
            Application.launch(args);
            view = new GUIView();
            view.startScreen();
            ClientApp clientApp = new ClientApp(view);
            clientApp.play();

        }
        System.exit(0);
    }





    /**
     * viene fatta la connessione al server con networkHandler.connectToServer();
     * viene creato un thread per l'eseguzione del clientController, questo thread viene interrotto
     * dopo che viene chiamato clientController.setDisconnected() in una qualsiasi parte del codice:
     * per fare ciò si è creato l'oggetto clientControllerLock su cui si fa wait, così nel metodo
     * clientController.setDisconnected() verrà chiamato notifyAll(), e si può procedere con l'interrupt()
     * del threadClientController
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
package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class GuiController extends Application {
    private Stage currentStage;
    private static GuiController guiController;
    private Runnable runnable;

    public static void main(String[] args) {
        Thread t = new Thread(() -> launch(args));
        t.start();
    }


    public static GuiController getInstance() {
        return guiController;
    }


    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * first method called: display gameModeFrame
     */
    @Override
    public void start(Stage newStage) throws Exception {
        guiController = this;
        Parent root = null;
        this.currentStage = newStage;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("chooseGameModeFrame.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.setTitle("Login");
        currentStage.getIcons().add(new Image("/images/imageStart/logo.png"));
        currentStage.setFullScreen(false);
        currentStage.setResizable(true);

        currentStage.show();
    }


    /**
     * SetNameCompleteObserver in GUIView is set with a consumer.
     * The consumer show an alert message if the value of its argument is false.
     * The SetNameCompleteObserver will be called in GUIView if the name is already in use (using accept(false))
     */
    public void switchNameScene() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/chooseNameFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //questo metodo dopo show muore tuttavia qui passa una callback da chiamre se serve alla guiView
        Consumer<Boolean> consumer = (ok) -> {
            Platform.runLater(() -> {
                if (!ok) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Name already in use", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        };
        ClientController.getInstance().getView().setNameCompleteObserver(consumer);
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.setFullScreen(false);
        currentStage.setResizable(true);
        currentStage.show();
    }

    public void switchWaitScene() {
        System.out.println("sto aspettando altri giocatori");
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/waitingPlayersFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.setTitle("ERIANTYS");
        currentStage.setFullScreen(false);
        currentStage.setResizable(true);
        currentStage.show();
    }


    public void switchGameScene() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/gamePianificationFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.setFullScreen(false);
        currentStage.setResizable(true);
        currentStage.show();
    }


    public void change() {
        Platform.runLater(runnable);
        runnable = null;
    }


    public void showGameUpdate() {
        ClientController clientController = ClientController.getInstance();
        switchGameScene();


            /*


             */

        }
        //to continue...



        /*
        List<CloudPojo> cloudPojos=clientController.getGameStatePojo().getClouds();
        for(CloudPojo c: cloudPojos){
            if(c.getCloudId()==0){
                redOnCloud1.setId(c.getStudents().getPawns().get(ColourPawn.Red).toString());
                yellowOnCloud1.setId(c.getStudents().getPawns().get(ColourPawn.Yellow).toString());
                blueOnCloud1.setId(c.getStudents().getPawns().get(ColourPawn.Blue).toString());
                greenOnCloud1.setId(c.getStudents().getPawns().get(ColourPawn.Green).toString());
            }
            if(c.getCloudId()==1){
                redOnCloud2.setId(c.getStudents().getPawns().get(ColourPawn.Red).toString());
                yellowOnCloud2.setId(c.getStudents().getPawns().get(ColourPawn.Yellow).toString());
                blueOnCloud2.setId(c.getStudents().getPawns().get(ColourPawn.Blue).toString());
                greenOnCloud2.setId(c.getStudents().getPawns().get(ColourPawn.Green).toString());
            }
            if(c.getCloudId()==2){
                redOnCloud3.setId(c.getStudents().getPawns().get(ColourPawn.Red).toString());
                yellowOnCloud3.setId(c.getStudents().getPawns().get(ColourPawn.Yellow).toString());
                blueOnCloud3.setId(c.getStudents().getPawns().get(ColourPawn.Blue).toString());
                greenOnCloud3.setId(c.getStudents().getPawns().get(ColourPawn.Green).toString());
            }
        }

*/
        }



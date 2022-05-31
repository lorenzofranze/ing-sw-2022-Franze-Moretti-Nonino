package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class GuiController extends Application {
    private Stage currentStage;
    private static GuiController guiController;
    private Runnable runnable;

    public static void main(String[] args) {
        Thread t = new Thread(()->launch(args));
        t.start();
    }


    public static GuiController getInstance(){
        return guiController;
    }


    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    /** first method called: display gameModeFrame */
    @Override
    public void start(Stage newStage) throws Exception {
        guiController = this;
        Parent root=null;
        this.currentStage=newStage;
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
        currentStage.sizeToScene();
        currentStage.show();
    }


    public void switchNameScene() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/chooseNameFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //questo metodo dopo show muore tuttavia qui passa una callback da chiamre se serve alla guiView
        ClientController.getInstance().getView().setNameCompleteObserver((ok) -> {
            Platform.runLater(() -> {
                if (!ok) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Name already in use", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        });
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.sizeToScene();
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
        currentStage.sizeToScene();
        currentStage.setMaximized(true);
        currentStage.show();
    }

    public void switchGameScene() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/gameActionFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.sizeToScene();
        currentStage.show();
    }


    public void change() {
        Platform.runLater(runnable);
        runnable=null;
    }
}

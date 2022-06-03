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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        currentStage.setResizable(false);
        currentStage.sizeToScene();
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
        currentStage.setResizable(false);
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
        currentStage.setFullScreen(true);
        currentStage.setResizable(true);
        currentStage.sizeToScene();
        currentStage.show();
    }


    public void switchGameScene() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/gameFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.setFullScreen(true);
        currentStage.setResizable(true);
        currentStage.sizeToScene();
        currentStage.show();
    }

    public void change() {
        Platform.runLater(runnable);
        runnable = null;
    }

    @FXML
    private ImageView AssistantCard1;
    @FXML
    private ImageView AssistantCard2;
    @FXML
    private ImageView AssistantCard3;
    @FXML
    private ImageView AssistantCard4;
    @FXML
    private ImageView AssistantCard5;
    @FXML
    private ImageView AssistantCard6;
    @FXML
    private ImageView AssistantCard7;
    @FXML
    private ImageView AssistantCard8;
    @FXML
    private ImageView AssistantCard9;
    @FXML
    private ImageView AssistantCard10;


    public void showGameUpdate() {
        AnchorPane anchorPane;
        ClientController clientController = ClientController.getInstance();
        PlayerPojo me = null;
        for (PlayerPojo p : clientController.getGameStatePojo().getPlayers()) {
            if (p.getNickname().equals(ClientController.getInstance().getNickname())) {
                me = p;
            }
        }

        //-----------------show assistant cards:
        List<ImageView> allCards = new ArrayList<>();
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#AssistantCards");
        allCards= anchorPane.getChildren().stream().map(a-> (ImageView)a).collect(Collectors.toList());
        //default: all card are visible and clickable, disable the used cards
        for(ImageView card : allCards){
            if(!me.getDeck().stream().map(a->a.getTurnOrder()).collect(Collectors.toList()).contains(Integer.parseInt(card.getId().substring(13)))) {
                card.setStyle("-fx-opacity: 30%");
            }
        }

        //------------- show boards:













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
}



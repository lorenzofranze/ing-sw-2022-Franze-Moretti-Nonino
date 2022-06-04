package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.model.PawnsMap;
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
        currentStage.setResizable(true);
        currentStage.centerOnScreen();
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
        currentStage.centerOnScreen();
        currentStage.setResizable(true);
        currentStage.sizeToScene();
        currentStage.show();
    }

    public void change() {
        Platform.runLater(runnable);
        runnable = null;
    }


    public void showGameUpdate() {

        AnchorPane anchorPane;
        /*
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/gameFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        root.lookup("#AssistantCards");
        anchorPane = (AnchorPane)currentStage.getScene().lookup("#AssistantCards") ;
        anchorPane =(AnchorPane) root.lookup("#AssistantCards");
         */

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

        /*
        //------------- show boards:
        ImageView professorRed = new ImageView(new Image("/images/pawns/teacher_red.png"));
        ImageView professorPink = new ImageView(new Image("/images/pawns/teacher_pink.png"));
        ImageView professorYellow = new ImageView(new Image("/images/pawns/teacher_yellow.png"));
        ImageView professorBlue = new ImageView(new Image("/images/pawns/teacher_blue.png"));
        ImageView professorGreen = new ImageView(new Image("/images/pawns/teacher_green.png"));

        List<PlayerPojo> players= clientController.getGameStatePojo().getPlayers();

        //__________ prima plancia
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#Plancia1");
        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Red)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorRedPlancia1");
        }

        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Blue)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorBluePlancia1");
        }

        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Yellow)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorYellowPlancia1");
        }

        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Pink)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorPinkPlancia1");
        }

        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Green)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorGreenPlancia1");
        }

        //--------- seconda plancia
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#Plancia2");
        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Red)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorRedPlancia2");
            p.setStyle("-fx-opacity: 30%");
        }

        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Blue)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorBluePlancia2");
            p.setStyle("-fx-opacity: 30%");
        }

        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Yellow)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorYellowPlancia2");
            p.setStyle("-fx-opacity: 30%");
        }

        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Pink)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorPinkPlancia2");
            p.setStyle("-fx-opacity: 30%");
        }

        if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Green)>0){
            ImageView p = (ImageView) currentStage.getScene().lookup("#professorGreenPlancia2");
            p.setStyle("-fx-opacity: 30%");
        }


        //---------- terza plancia
        if(clientController.getGameStatePojo().getPlayers().size()==3) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#Plancia3");
            if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Red)>0){
                ImageView p = (ImageView) currentStage.getScene().lookup("#professorRedPlancia3");
                p.setStyle("-fx-opacity: 30%");
            }

            if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Blue)>0){
                ImageView p = (ImageView) currentStage.getScene().lookup("#professorBluePlancia3");
                p.setStyle("-fx-opacity: 30%");
            }

            if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Yellow)>0){
                ImageView p = (ImageView) currentStage.getScene().lookup("#professorYellowPlancia3");
                p.setStyle("-fx-opacity: 30%");
            }

            if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Pink)>0){
                ImageView p = (ImageView) currentStage.getScene().lookup("#professorPinkPlancia3");
                p.setStyle("-fx-opacity: 30%");
            }

            if (players.get(0).getSchoolBoard().getProfessors().getPawns().get(ColourPawn.Green)>0){
                ImageView p = (ImageView) currentStage.getScene().lookup("#professorGreenPlancia3");
                p.setStyle("-fx-opacity: 30%");
            }

        }*/
        //--------- entrance
        AnchorPane child;
        int i=0;
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#entrance1");
        for(ImageView image : getInstance().
                PawnsToImageStudents(ClientController.getInstance().getGameStatePojo().getPlayers().get(0).getSchoolBoard().getEntrance())){
            i++;
            child = anchorPane.getChildren().stream().map(a->(AnchorPane)a).collect(Collectors.toList()).get(i);
            child.getChildren().add(image);
        }
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#entrance2");
        i=0;
        for(ImageView image : getInstance().
                PawnsToImageStudents(ClientController.getInstance().getGameStatePojo().getPlayers().get(0).getSchoolBoard().getEntrance())){
            i++;
            child = anchorPane.getChildren().stream().map(a->(AnchorPane)a).collect(Collectors.toList()).get(i);
            child.getChildren().add(image);
        }
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#entrance3");
        i=0;
        for(ImageView image : getInstance().
                PawnsToImageStudents(ClientController.getInstance().getGameStatePojo().getPlayers().get(0).getSchoolBoard().getEntrance())){
            i++;
            child = anchorPane.getChildren().stream().map(a->(AnchorPane)a).collect(Collectors.toList()).get(i);
            child.getChildren().add(image);
        }
    }

    /**receives in input a pawnsmap and returns the corrisponding  List of images */
    private List<ImageView> PawnsToImageStudents(PawnsMapPojo map){
        List<ImageView> list = new ArrayList<>();
        int toAdd;
        String prefix = "/images/pawns/";
        String path="";
        for(ColourPawn colourPawn : ColourPawn.values()){
            if(colourPawn==ColourPawn.Yellow){
                path=prefix+"student_yellow.png";
            }else if(colourPawn == ColourPawn.Blue){
                path = prefix+"student_blue.png";
            }else if(colourPawn == ColourPawn.Pink){
                path = prefix+"student_pink.png";
            }else if(colourPawn == ColourPawn.Green){
                path = prefix+"student_green.png";
            }else if(colourPawn == ColourPawn.Red){
                path = prefix + "student_red.png";
            }
            toAdd = map.get(colourPawn);
            for(int i =0; i<toAdd; i++){
                list.add(new ImageView(new Image(path)));
            }
        }
        return list;
    }















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

package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GuiController extends Application {
    private Stage currentStage;
    private static GuiController guiController;
    private Runnable runnable;
    private boolean first = true;



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
            root = FXMLLoader.load(getClass().getResource("/waitingPlayersFrameBrutto.fxml"));
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
        //executed only the first time: sets fixed elements (e.g. number of boards)
        if(this.first){
            this.first=false;
            initializeTable();
        }
        GameStatePojo game = ClientController.getInstance().getGameStatePojo();
        int numPlayers = game.getPlayers().size();

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



        //--------- entrance
        AnchorPane child;
        int i=0;

        for (int j=1; j<numPlayers+1; j++){
             i=0;
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#entrance"+j);
            for(ImageView image : getInstance().
                    PawnsToImageStudents(ClientController.getInstance().getGameStatePojo().getPlayers().get(j-1).getSchoolBoard().getEntrance())){
                i++;
                child = anchorPane.getChildren().stream().map(a->(AnchorPane)a).collect(Collectors.toList()).get(i);
                child.getChildren().add(image);
            }
        }

        //--------- professors
        GridPane gridPane;
        int posiz;

        for (int j=1; j<numPlayers+1; j++){
            i=0;
            posiz=0;
            gridPane = (GridPane) currentStage.getScene().lookup("#professorsPlancia"+j);
            List<ImageView> images = PawnsToImageProfessor(game.getPlayers().get(i).getSchoolBoard().getProfessors());
            for(ColourPawn colour : ColourPawn.values()){
                for (int k = 0; k < game.getPlayers().get(i).getSchoolBoard().getProfessors().get(colour); k++){
                    gridPane.add(images.get(posiz), colour.getIndexColour(),k);
                    posiz++;
                }
            }
        }

        // ------ students in dining
        for (int j=1; j<numPlayers+1; j++) {
            i = 0;
            posiz=0;
            gridPane = (GridPane) currentStage.getScene().lookup("#studentsPlancia"+j);
            List<ImageView> images = PawnsToImageStudents(game.getPlayers().get(i).getSchoolBoard().getDiningRoom());
            for(ColourPawn colour : ColourPawn.values()){
                for (int k = 0; k < game.getPlayers().get(i).getSchoolBoard().getDiningRoom().get(colour); k++){
                    gridPane.add(images.get(posiz),k, colour.getIndexColour());
                    posiz++;
                }
            }
        }

        //------- towers
        int k;
        String path="";
        ImageView imageView;
        for (int j=1; j<numPlayers+1; j++) {
            i = 0;
            if (j == 1) {
                path = "/images/pawns/black_tower.png";
            } else if (j == 2) {
                path = "/images/pawns/white_tower.png";
            } else if (j == 3) {
                path = "/images/pawns/grey_tower.png";
            }
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#towersPlancia" + j);
            for (k = 0; k < ClientController.getInstance().getGameStatePojo().getPlayers().get(j - 1).getSchoolBoard().getSpareTowers(); k++) {
                imageView = anchorPane.getChildren().stream().map(a -> (ImageView) a).collect(Collectors.toList()).get(i);
                imageView.setImage(new Image(path));
                i++;
            }


        }

        //-----islands:
        //student

        //----- clouds
        for (int j=1; j<numPlayers+1; j++) {
            i = 1;
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#cloud" + j);
            for (ImageView image :
                    PawnsToImageStudents(ClientController.getInstance().getGameStatePojo().getClouds().get(j-1).getStudents())) {
                child = anchorPane.getChildren().stream().map(a -> (AnchorPane) a).collect(Collectors.toList()).get(i);
                child.getChildren().add(image);
                i++;
            }
        }

        //------- characters
        if(game.isExpert()) {
            i = 0;
            SplitPane splitPane = (SplitPane) currentStage.getScene().lookup("#charactersPane");
            for (Image image :
                    CharactersToImage(ClientController.getInstance().getGameStatePojo().getCharacters())) {
                child = splitPane.getItems().stream().map(a -> (AnchorPane) a).collect(Collectors.toList()).get(i);
                ((ImageView)child.getChildren().get(0)).setImage(image);
                i++;
            }
        }


    }
    /**initializes the view with fixed elements during the game (e.g. nicknames on boards and number of clouds) */
    private void initializeTable(){
        //tab of schoolBoards:
        TabPane tabPane;
        GameStatePojo game = ClientController.getInstance().getGameStatePojo();
        int numPlayers = game.getPlayers().size();
        //hide board 3
        tabPane = (TabPane) currentStage.getScene().lookup("#boards");
        if(numPlayers ==2){
            tabPane.getTabs().remove(2);
        }
        //set nicknames on boards
        int i=0;
        for(PlayerPojo player : game.getPlayers()){
            tabPane.getTabs().get(i).setText(player.getNickname());
            i++;
        }
        //clouds:
        AnchorPane anchorPane;
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#cloud3");
        if(numPlayers ==2){
            anchorPane.setVisible(false);
        }
        //hide character card if simple mode
        if(game.isExpert()==false){
            ((SplitPane) currentStage.getScene().lookup("#charactersPane")).setVisible(false);
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

    /**receives in input a pawnsmap and returns the corrisponding  List of images */
    private List<ImageView> PawnsToImageProfessor( PawnsMapPojo map){
        List<ImageView> list = new ArrayList<>();
        int toAdd;
        String prefix = "/images/pawns/";
        String path="";
        for(ColourPawn colourPawn : ColourPawn.values()){
            if(colourPawn==ColourPawn.Yellow){
                path=prefix+"teacher_yellow.png";
            }else if(colourPawn == ColourPawn.Blue){
                path = prefix+"teacher_blue.png";
            }else if(colourPawn == ColourPawn.Pink){
                path = prefix+"teacher_pink.png";
            }else if(colourPawn == ColourPawn.Green){
                path = prefix+"teacher_green.png";
            }else if(colourPawn == ColourPawn.Red){
                path = prefix + "teacher_red.png";
            }
            toAdd = map.get(colourPawn);
            for(int i =0; i<toAdd; i++){
                list.add(new ImageView(new Image(path)));
            }
        }
        return list;
    }

    //**todo dimensioni fissate
    /**receives in input a pawnsmap and returns the corrisponding  List of images */
    private List<Image> CharactersToImage(List<CharacterPojo> charactersList){
        List<Image> list = new ArrayList<>();
        int toAdd;
        String prefix = "/images/imageCharacters/CarteTOT_front";
        String path="";
        for(CharacterPojo c : charactersList){
            path=prefix+(c.getCharacterId())+".jpg";
            list.add(new Image(path));
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

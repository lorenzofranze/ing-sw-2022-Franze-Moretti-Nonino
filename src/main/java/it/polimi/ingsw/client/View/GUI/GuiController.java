package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.model.Island;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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

    public void runMethod() {
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
                child = anchorPane.getChildren().stream().map(a->(AnchorPane)a).collect(Collectors.toList()).get(i);
                child.getChildren().add(image);
                i++;
                //set drag event
                image.setOnDragDetected(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        System.out.println("inizio drag");
                        GameHandlerScene.setStudentChosen(event);
                    }
                });

            }
        }

        //--------- professors
        GridPane gridPane;
        int posiz;

        for (int j=1; j<numPlayers+1; j++){
            i=0;
            posiz=0;
            gridPane = (GridPane) currentStage.getScene().lookup("#professorsPlancia"+j);
            List<Image> images = PawnsToImageProfessor(game.getPlayers().get(j-1).getSchoolBoard().getProfessors());
            for(ImageView imageView : gridPane.getChildren().stream().map(a->(ImageView)a).collect(Collectors.toList())){
                if (game.getPlayers().get(j-1).getSchoolBoard().getProfessors().get(ColourPawn.values()[i])==1){
                    imageView.setImage(images.get(posiz));
                    posiz++;
                }
                i++;
            }
        }

        // ------ students in dining
        for (int j=1; j<numPlayers+1; j++) {
            posiz=0;
            gridPane = (GridPane) currentStage.getScene().lookup("#studentsPlancia"+j);
            List<ImageView> images = PawnsToImageStudents(game.getPlayers().get(j-1).getSchoolBoard().getDiningRoom());
            for(ColourPawn colour : ColourPawn.values()){
                for (int k = 0; k < game.getPlayers().get(j-1).getSchoolBoard().getDiningRoom().get(colour); k++){
                    gridPane.add(images.get(posiz),k, colour.getIndexColour());
                    posiz++;
                }
            }
        }

        //------- towers on boards:
        int k;
        String path="";
        ImageView imageView;
        for (int j=1; j<numPlayers+1; j++) {
            i = 0;
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#towersPlancia" + j);
            for (k = 0; k < ClientController.getInstance().getGameStatePojo().getPlayers().get(j - 1).getSchoolBoard().getSpareTowers(); k++) {
                imageView = anchorPane.getChildren().stream().map(a -> (ImageView) a).collect(Collectors.toList()).get(i);
                Image image = towerToImage(game.getPlayers().get(j-1).getColourTower());
                imageView.setImage(image);
                i++;
            }


        }

        //-----islands
        for(int j=1; j<13; j++){
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#island"+j);
            anchorPane.setVisible(false);
        }

        //students on islands:
        for(int j=1; j<game.getIslands().size()+1; j++){
            //maybe to remove when function reset done
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#island"+j);
            anchorPane.setVisible(true);
            //--
            i=0;
            k=0;
            GridPane studentPane = (GridPane) anchorPane.getChildren().get(1);
            List<ImageView> students = PawnsToImageStudents(game.getIslands().get(j-1).getStudents());
            for(ImageView image : students) {
                image.setFitHeight(117.0);
                image.setFitWidth(22.0);
                image.setPreserveRatio(true);
                studentPane.add(image, i, k);
                k++;
                if(k==5){
                    i++;
                    k=0;
                }
            }
        }
        //mother nature:
        GridPane towersPane;
        int positionMother=0;
        k=0;
        for(IslandPojo island : game.getIslands()){
            if(island.isHasMotherNature()){
                positionMother=k;
            }
            k++;
        }
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#island"+(positionMother+1));
        towersPane = (GridPane) anchorPane.getChildren().get(2);
        imageView = new ImageView(new Image("/images/pawns/mother_nature.png"));
        imageView.setFitHeight(48.0);
        imageView.setFitWidth(58.0);
        imageView.setPreserveRatio(true);
        towersPane.add(imageView, 0, 0);

        //towers on islands:
        for(int j=1; j<game.getIslands().size()+1; j++){
            if(game.getIslands().get(j-1).getTowerColour()!=null) {
                anchorPane = (AnchorPane) currentStage.getScene().lookup("#island" + j);
                towersPane = (GridPane) anchorPane.getChildren().get(2);

                Image image = towerToImage(game.getIslands().get(j - 1).getTowerColour());
                imageView = new ImageView(image);
                imageView.setFitHeight(47.0);
                imageView.setFitWidth(58.0);
                imageView.setPreserveRatio(true);
                towersPane.add(new ImageView(image), 1, 0);
                Text text = new Text("   "+game.getIslands().get(j - 1).getTowerCount());
                text.setTextAlignment(TextAlignment.CENTER);
                text.setFont(Font.font(20.0));
                towersPane.add(text, 2, 0);
            }
        }



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
        //hide coins element if simple mode
        if(game.isExpert()==false){
            ( currentStage.getScene().lookup("#coinsPlayer")).setVisible(false);
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
                ImageView imageView = new ImageView(new Image(path));
                imageView.setUserData(colourPawn);
                list.add(imageView);
            }
        }
        return list;
    }

    /**receives in input a pawnsmap and returns the corrisponding  List of images */
    private List<Image> PawnsToImageProfessor( PawnsMapPojo map){
        List<Image> list = new ArrayList<>();
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
                list.add(new Image(path));
            }
        }
        return list;
    }


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

    private Image towerToImage(ColourTower colour){
        String path="";
        if (colour == ColourTower.Black) {
            path = "/images/pawns/black_tower.png";
        } else if (colour == ColourTower.White) {
            path = "/images/pawns/white_tower.png";
        } else if (colour == ColourTower.Grey) {
            path = "/images/pawns/grey_tower.png";
        }
        return (new Image(path));
    }

    /**sets the coins pane visible with right coins for the player choosen */
    public void activeCoins(int index) {
        AnchorPane anchorPane;
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#coinsPlayer");
        //set text to player's coins
        Text text = (Text)anchorPane.getChildren().get(1);
        text.setText(ClientController.getInstance().getGameStatePojo().getPlayers().get(index-1).getCoins()+"");
        //set drag event handeler on imageView: ...
    }






    }

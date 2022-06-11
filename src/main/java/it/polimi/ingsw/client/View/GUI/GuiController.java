package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.ClientApp;
import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.model.Island;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GuiController extends Application {
    private Stage currentStage;
    private static GuiController guiController;
    private Runnable runnable;
    private boolean first = true;
    private Scene currentScene;


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
        //close button
        currentStage.setOnCloseRequest(event ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to leave the game?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.CANCEL) {
                    event.consume();
                }else{
                    System.exit(0);
                }
            });
        });
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
        currentStage.setTitle("ERIANTYS ["+ClientController.getInstance().getNickname()+"]");
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
        currentScene = scene;
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
        if (this.first) {
            this.first = false;
            initializeTable();
            //sets utility attributes in GameHandlerScene
            GameHandlerScene.setMyOrderInPlayers();
        }

        reset();

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
        allCards = anchorPane.getChildren().stream().map(a -> (ImageView) a).collect(Collectors.toList());
        //default: all card are visible and clickable, disable the used cards
        for (ImageView card : allCards) {
            if (!me.getDeck().stream().map(a -> a.getTurnOrder()).collect(Collectors.toList()).contains(Integer.parseInt(card.getId().substring(13)))) {
                card.setStyle("-fx-opacity: 30%");
            }
        }


        //--------- entrance
        AnchorPane child;
        int i = 0;

        for (int j = 1; j < numPlayers + 1; j++) {
            i = 0;
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#entrance" + j);
            for (ImageView image : getInstance().
                    PawnsToImageStudents(ClientController.getInstance().getGameStatePojo().getPlayers().get(j - 1).getSchoolBoard().getEntrance())) {
                child = anchorPane.getChildren().stream().map(a -> (AnchorPane) a).collect(Collectors.toList()).get(i);
                child.getChildren().add(image);
                i++;
                //set drag event
                image.setOnDragDetected(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        GameHandlerScene.setStudentChosen(event);
                    }
                });

            }
        }

        //--------- professors
        GridPane gridPane;
        int posiz;

        for (int j = 1; j < numPlayers + 1; j++) {
            i = 0;
            posiz = 0;
            gridPane = (GridPane) currentStage.getScene().lookup("#professorsPlancia" + j);
            List<Image> images = PawnsToImageProfessor(game.getPlayers().get(j - 1).getSchoolBoard().getProfessors());
            for (ImageView imageView : gridPane.getChildren().stream().map(a -> (ImageView) a).collect(Collectors.toList())) {
                if (game.getPlayers().get(j - 1).getSchoolBoard().getProfessors().get(ColourPawn.values()[i]) == 1) {
                    imageView.setImage(images.get(posiz));
                    posiz++;
                }
                i++;
            }
        }

        // ------ students in dining
        for (int j = 1; j < numPlayers + 1; j++) {
            posiz = 0;
            gridPane = (GridPane) currentStage.getScene().lookup("#studentsPlancia" + j);
            List<ImageView> images = PawnsToImageStudents(game.getPlayers().get(j - 1).getSchoolBoard().getDiningRoom());
            for (ColourPawn colour : ColourPawn.values()) {
                for (int k = 0; k < game.getPlayers().get(j - 1).getSchoolBoard().getDiningRoom().get(colour); k++) {
                    gridPane.add(images.get(posiz), k, colour.getIndexColour());
                    posiz++;
                }
            }
        }

        //------- towers on boards:
        int k;
        String path = "";
        ImageView imageView;
        for (int j = 1; j < numPlayers + 1; j++) {
            i = 0;
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#towersPlancia" + j);
            for (k = 0; k < ClientController.getInstance().getGameStatePojo().getPlayers().get(j - 1).getSchoolBoard().getSpareTowers(); k++) {
                imageView =(ImageView) anchorPane.getChildren().get(i);
                Image image = towerToImage(game.getPlayers().get(j - 1).getColourTower());
                imageView.setImage(image);
                i++;
            }
        }
        //students on islands:
        for (int j = 1; j < game.getIslands().size() + 1; j++) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#island" + j);
            i = 0;
            k = 0;
            GridPane studentPane = (GridPane) anchorPane.getChildren().get(1);
            List<ImageView> students = PawnsToImageStudents(game.getIslands().get(j - 1).getStudents());
            for (ImageView image : students) {
                image.setFitWidth(19.0);
                image.setPreserveRatio(true);
                studentPane.add(image, i, k);
                k++;
                if (k == 6) {
                    i++;
                    k = 0;
                }
            }
        }
        //mother nature:
        GridPane towersPane;
        int positionMother = 0;
        k = 0;
        for (IslandPojo island : game.getIslands()) {
            if (island.isHasMotherNature()) {
                positionMother = k;
            }
            k++;
        }
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#island" + (positionMother + 1));
        towersPane = (GridPane) anchorPane.getChildren().get(2);
        imageView = new ImageView(new Image("/images/pawns/mother_nature.png"));
        imageView.setId("motherNaturePawn");
        imageView.setFitHeight(48.0);
        imageView.setFitWidth(58.0);
        imageView.setPreserveRatio(true);
        towersPane.add(imageView, 0, 0);

        //towers on islands:
        for (int j = 1; j < game.getIslands().size() + 1; j++) {
            if (game.getIslands().get(j - 1).getTowerColour() != null) {
                anchorPane = (AnchorPane) currentStage.getScene().lookup("#island" + j);
                towersPane = (GridPane) anchorPane.getChildren().get(2);

                Image image = towerToImage(game.getIslands().get(j - 1).getTowerColour());
                imageView = new ImageView(image);
                imageView.setRotate(-90.0);
                imageView.setFitHeight(48.0);
                imageView.setFitWidth(58.0);
                imageView.setPreserveRatio(true);
                towersPane.add(imageView, 0,1);

                Text text = new Text("   " + game.getIslands().get(j - 1).getTowerCount());
                text.setTextAlignment(TextAlignment.CENTER);
                text.setFont(Font.font(20.0));
                towersPane.add(text, 0,2);
            }
        }


        //----- clouds
        for (int j = 1; j < numPlayers + 1; j++) {
            i = 1;
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#cloud" + j);
            for (ImageView image :
                    PawnsToImageStudents(ClientController.getInstance().getGameStatePojo().getClouds().get(j - 1).getStudents())) {
                child = anchorPane.getChildren().stream().map(a -> (AnchorPane) a).collect(Collectors.toList()).get(i);
                child.getChildren().add(image);
                i++;
            }
        }

        //------ coins left
        if (game.isExpert()) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#coinsTable");
            //set text to player's coins
            Text text = (Text) anchorPane.getChildren().get(1);
            text.setText(ClientController.getInstance().getGameStatePojo().getCoinSupply() + "");
        }

        //------ turn order
        Label label = (Label) currentStage.getScene().lookup("#turnLabel");
        String turnPlayer = ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname();
        label.setText("TURN: " + turnPlayer);

        //update assistant card played
        int index =  ((TabPane)currentStage.getScene().lookup("#boards")).getSelectionModel().getSelectedIndex()+1;
        if(ClientController.getInstance().getGameStatePojo().getPlayers().get(index-1).getPlayedAssistantCard()!=null) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#detailsPlancia");
            int card = ClientController.getInstance().getGameStatePojo().getPlayers().get(index - 1).getPlayedAssistantCard().getTurnOrder();
            path = "/images/imageGamePianification/Assistente (" + card + ").png";
            ((ImageView) anchorPane.getChildren().get(2)).setImage(new Image(path));
        }
        if(game.isExpert()) {
            updateCharacterCards();
        }
    }

    /**reste the view to initial condition: done before each update event */
    private void reset(){
        GameStatePojo game = ClientController.getInstance().getGameStatePojo();
        int numPlayers = game.getPlayers().size();
        AnchorPane anchorPane;
        AnchorPane child;
        int i=0;
        int j;
        GridPane gridPane;
        int posiz;
        //--------- entrance
        for (j=1; j<numPlayers+1; j++){
            i=0;
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#entrance"+j);
            for(AnchorPane position : anchorPane.getChildren().stream().map(a->(AnchorPane)a).collect(Collectors.toList())){
                position.getChildren().clear();
            }
        }
        //--------- professors
        for (j=1; j<numPlayers+1; j++){
            i=0;
            posiz=0;
            gridPane = (GridPane) currentStage.getScene().lookup("#professorsPlancia"+j);
            for(ImageView imageView : gridPane.getChildren().stream().map(a->(ImageView)a).collect(Collectors.toList())){
                imageView.setImage(null);
            }
        }
        // ------ students in dining
        for (j=1; j<numPlayers+1; j++) {
            posiz=0;
            gridPane = (GridPane) currentStage.getScene().lookup("#studentsPlancia"+j);
            gridPane.getChildren().clear();
        }

        //------- towers on boards:
        int k;
        String path="";
        ImageView imageView;
        for (j=1; j<numPlayers+1; j++) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#towersPlancia" + j);
            anchorPane.getChildren().stream().map(a->(ImageView)a).forEach(a->a.setImage(null));
        }
        //-----islands
        for(j=game.getIslands().size()+1; j<13; j++){
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#island"+j);
            anchorPane.setVisible(false);
        }
        //students, towers and mother nature on islands
        for(j=1; j<game.getIslands().size()+1; j++){
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#island"+j);
            GridPane studentPane = (GridPane) anchorPane.getChildren().get(1);
            studentPane.getChildren().clear();GridPane towersPane;
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#island"+j);
            towersPane = (GridPane) anchorPane.getChildren().get(2);
            towersPane.getChildren().clear();
        }
        //----- clouds
        for (j=1; j<numPlayers+1; j++) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#cloud" + j);
            for (i=1; i<5; i++) {
                child = (AnchorPane) anchorPane.getChildren().get(i);
                child.getChildren().clear();
            }
        }
        //reset assistant card view
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#detailsPlancia");
        ((ImageView)anchorPane.getChildren().get(2)).setImage(null);
        if(game.isExpert()) {
            resetCharacterCards();
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
        //set character images
        AnchorPane child;
        if(game.isExpert()) {
            i = 0;
            SplitPane splitPane = (SplitPane) currentStage.getScene().lookup("#charactersPane");
            for(CharacterPojo characterPojo : ClientController.getInstance().getGameStatePojo().getCharacters()){
                child = (AnchorPane) splitPane.getItems().get(i);
                //set image
                ((ImageView)child.getChildren().get(0)).setImage(charactersToImage(characterPojo));
                //set id
                child.setId("card"+characterPojo.getCharacterId());
                i++;
                //hide coin incremented
                ((ImageView)child.getChildren().get(2)).setVisible(false);
            }
        }
        //hide coins left if simple
        if(game.isExpert()==false){
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#coinsTable");
            anchorPane.setVisible(false);
        }

        //hide coins of player if simple mode
        if(game.isExpert()==false){
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#detailsPlancia");
            anchorPane.getChildren().get(0).setVisible(false);
            anchorPane.getChildren().get(1).setVisible(false);
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
    private Image charactersToImage(CharacterPojo character){
        String prefix = "/images/imageCharacters/CarteTOT_front";
        String path="";
        path=prefix+(character.getCharacterId())+".jpg";
        return new Image(path);
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
    public void activeDetails(int index) {
        AnchorPane anchorPane;
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#detailsPlancia");
        //set card played
        if(ClientController.getInstance().getGameStatePojo().getPlayers().get(index-1).getPlayedAssistantCard()==null) {
            ((ImageView)anchorPane.getChildren().get(2)).setImage(null);
        }else {
            int card = ClientController.getInstance().getGameStatePojo().getPlayers().get(index - 1).getPlayedAssistantCard().getTurnOrder();
            String path = "/images/imageGamePianification/Assistente (" + card + ").png";
            ((ImageView) anchorPane.getChildren().get(2)).setImage(new Image(path));
        }
        if(ClientController.getInstance().getGameStatePojo().isExpert()) {
            //set text to player's coins
            Text text = (Text) anchorPane.getChildren().get(1);
            text.setText(ClientController.getInstance().getGameStatePojo().getPlayers().get(index - 1).getCoins() + "");
            //set drag event handeler on imageView: ...
        }
    }

    public void changeCursor(int val){
        if(val==0){
            currentScene.setCursor(Cursor.DEFAULT);
        }else if(val==1) {
            currentScene.setCursor(new ImageCursor(new Image("/images/pawns/mother_nature.png")));
        }
    }

    public void activeGuiCard9(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        //Setting the title
        alert.setTitle("CARD 9");
        ButtonType red = new ButtonType("red", ButtonBar.ButtonData.OK_DONE);
        ButtonType yellow = new ButtonType("yellow", ButtonBar.ButtonData.OK_DONE);
        ButtonType green = new ButtonType("green", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        alert.setContentText("la carta 9 fa... scegli un colore");
        //Adding buttons to the dialog pane
        alert.getDialogPane().getButtonTypes().add(red);
        alert.getDialogPane().getButtonTypes().add(yellow);
        alert.getDialogPane().getButtonTypes().add(green);
    }

    private void updateCharacterCards(){
        //note: each anchorPane has an ID : "card"+(num. card): once you have the anchorPane do anchorPane.getChildren().get(1)
        // to get the gridPane with students or no entry tiles


    }

    private void resetCharacterCards(){

    }






}

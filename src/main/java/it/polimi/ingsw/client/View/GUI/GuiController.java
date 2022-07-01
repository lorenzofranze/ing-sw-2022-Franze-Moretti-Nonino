package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.ClientApp;
import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.Island;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/** GUI's core: this class handles the interaction of the player with GUI: show new scenes, show alerts.
 * The most important methos is update() used for update the view of all the players during the game
 */
public class GuiController extends Application {
    private static Stage currentStage;
    private static GuiController guiController;
    private static boolean otherPlayerDisconnected;
    private static boolean pingUnreceived;
    private Runnable runnable;
    private boolean first = true;
    private Scene currentScene;
    private static boolean noErrors = false;


    public static void main(String[] args) {
        Thread t = new Thread(() -> launch(args));
        t.start();
    }


    public static GuiController getInstance() {
        return guiController;
    }

    public static void setOtherPlayerDisconnected(boolean b) {
        otherPlayerDisconnected = true;
    }

    public static void setPingUnreceived() {
        pingUnreceived = true;
    }


    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * First method called: display gameModeFrame
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
        GameHandlerScene.setStage(currentStage);
    }


    /**
     * Switch to the name scene (where the players choose their nickname)
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

    /**
     * Switch to the waiting scene (where the players wait for the lobby to receive other players)
     */
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
        ImageView imageView= (ImageView) currentStage.getScene().lookup("#catHead");
        RotateTransition rotateTransition= new RotateTransition(Duration.millis(3500), imageView);
        rotateTransition.setByAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);


        rotateTransition.play();
    }


    /**
     * Switch to the game scene (where the players play the game)
     */
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

    /**
     * Switch to the end-game scene (where the winner is displayed)
     */
    public void switchGameOverScene() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/gameOverFrame.fxml"));
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
        GameStatePojo gameStatePojo = ClientController.getInstance().getGameStatePojo();
        if (gameStatePojo.isGameOver() && gameStatePojo.getWinner() != null) {

            Label label = (Label) currentStage.getScene().lookup("#textGameOver");
            if (gameStatePojo.getWinner().equals("?")) {
                label.setText("Draw!");
            } else {
                if (gameStatePojo.getWinner().equals(ClientController.getInstance().getNickname())) {
                    label.setText("Congratulations! You have won");
                } else {
                    label.setText("The winner is: " + gameStatePojo.getWinner() +
                            "\n Better luck next time");
                }

            }
        }
        currentStage.show();
    }


    public void runMethod() {
        Platform.runLater(runnable);
        runnable = null;
    }


    /**
     * If it is the first game update --> calls initializeTable(): executed only the first time: sets fixed elements
     * (e.g. number of boards) and sets utility attributes in GameHandlerScene.
     * All the times --> shows
     * -assistant cards
     * -entrance
     * -professors
     * -students in dining
     * -towers on boards
     * -students on islands
     * -mother nature
     *  -towers on islands
     *  -clouds
     *  -coins left (if expert)
     *  -turn order
     *  -nickname label
     *  -student bag
     *  -assistant card played
     *  - if expert: set text to player's coins and updateCharacterCards()
     */
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
            if(ClientController.getInstance().getGameStatePojo().getClouds().get(j - 1).getStudents().pawnsNumber()==0){
                anchorPane.setDisable(true);
            }else{
                anchorPane.setDisable(false);
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


        //nickname label
        label = (Label) currentStage.getScene().lookup("#playerLabel");
        turnPlayer = ClientController.getInstance().getNickname();
        label.setText("PLAYER: " + turnPlayer);

        //student bag
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#studentsTable");
        Text text = (Text) anchorPane.getChildren().get(1);
        text.setText(String.valueOf(ClientController.getInstance().getGameStatePojo().getStudentsBag().pawnsNumber()));

        //update assistant card played
        int index =  ((TabPane)currentStage.getScene().lookup("#boards")).getSelectionModel().getSelectedIndex()+1;
        if(ClientController.getInstance().getGameStatePojo().getPlayers().get(index-1).getPlayedAssistantCard()!=null) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#detailsPlancia");
            int card = ClientController.getInstance().getGameStatePojo().getPlayers().get(index - 1).getPlayedAssistantCard().getTurnOrder();
            path = "/images/imageGamePianification/Assistente (" + card + ").png";
            ((ImageView) anchorPane.getChildren().get(2)).setImage(new Image(path));
        }

        //set text to player's coins
        if(game.isExpert()) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#detailsPlancia");
            text = (Text) anchorPane.getChildren().get(1);
            text.setText(ClientController.getInstance().getGameStatePojo().getPlayers().get(index - 1).getCoins() + "");
        }

        //update characters
        if(game.isExpert()) {
            updateCharacterCards();
        }

    }

    /**
     * Reset the view to initial condition: done before each update event so that elements do not
     * goes one over the other */
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
            studentPane.getChildren().clear();
            GridPane towersPane;
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

    /**
     * Initializes the view with fixed elements during the game (e.g. nicknames on boards and number of clouds) */
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
            ((AnchorPane) currentStage.getScene().lookup("#cardsDescritpionButton")).setVisible(false);
        }
        //set character images
        AnchorPane child;
        if(game.isExpert()) {
            i = 0;
            SplitPane splitPane = (SplitPane) currentStage.getScene().lookup("#charactersPane");
            for(CharacterPojo characterPojo : ClientController.getInstance().getGameStatePojo().getCharacters()){
                child = (AnchorPane) splitPane.getItems().get(i);
                //set image
                ((ImageView)(child.getChildren().get(0))).setImage(charactersToImage(characterPojo));
                //set id
                child.setId("card"+characterPojo.getCharacterId());
                //hide coin incremented
                ((ImageView)child.getChildren().get(2)).setVisible(false);
                i++;
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
        //set coins property
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#detailsPlancia");
        ImageView image = (ImageView) (anchorPane.getChildren().get(0));
        image.setUserData("coins");
    }

    /**
     * Receives in input a pawnsmap and returns the corrisponding  List of images */
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
    /**receives in input a tower and returns the corrisponding image */
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

    /**sets the coins pane visible with right coins for the player choosen and its assistant card */
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
        }
    }

    /**
     * Changes the cursor image while dragging elements
     * @param val
     */
    public void changeCursor(int val){
        if(val==0){
            currentScene.setCursor(Cursor.DEFAULT);
        }else if(val==1) {
            currentScene.setCursor(new ImageCursor(new Image("/images/pawns/mother_nature.png")));
        }else if(val == 2){
            currentScene.setCursor(new ImageCursor(new Image("/images/imageCharacters/CarteTOT_front3.jpg")));
        }else if(val == 3){
            currentScene.setCursor(new ImageCursor(new Image("/images/imageCharacters/deny_island_icon.png")));
        }
    }




    //////////------------------------/////////////--------------//////////
    //                  COMPLEX MODE METHODS:                            //
    //////////------------------------/////////////--------------//////////

    /** Update character cards view (elements on the card):
     * -show coin on card if is incremented
     * -effect on card if is active
     * -puts no entry tiles on card
     * -puts no entry tiles on islands
     * -put pawns on  card*/
    private void updateCharacterCards() {
        //note: each anchorPane has an ID : "card"+(num. card): once you have the anchorPane do anchorPane.getChildren().get(1)
        // to get the gridPane with students or no entry tiles
        GameStatePojo game = ClientController.getInstance().getGameStatePojo();
        AnchorPane anchorPane;
        GridPane gridPane;
        ImageView imageView;
        int rig;
        int col;

        for (CharacterPojo card : game.getCharacters()) {
            if (card.isIncremented()) {
                //show coin on card if is incremented
                anchorPane = (AnchorPane) currentStage.getScene().lookup("#card" + card.getCharacterId());
                ((ImageView) anchorPane.getChildren().get(2)).setVisible(true);
            }
            //effect on card if is active
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#card" + card.getCharacterId());
            if(game.getActiveEffect() == null){
                anchorPane.getChildren().get(0).setEffect(null);
            }else if(game.getActiveEffect().getCharacterId() == card.getCharacterId()){
                Glow g = new Glow();
                g.setLevel(35.0);
                anchorPane.getChildren().get(0).setEffect(g);
            }




            anchorPane = (AnchorPane) currentStage.getScene().lookup("#card" + card.getCharacterId());

            if (card.getCharacterId() == 5) {

                rig = 0;
                col = 0;

                gridPane = (GridPane) anchorPane.getChildren().get(1);

                //puts no entry tiles on card
                for (int i = 0; i < card.getNumNoEntry(); i++) {
                    imageView = new ImageView(new Image("/images/imageCharacters/deny_island_icon.png"));
                    if (col == 3) {
                        col = 0;
                        rig = 1;
                    }
                    imageView.setFitWidth(30.0);
                    imageView.setPreserveRatio(true);
                    gridPane.add(imageView, col, rig);
                    col++;
                }
                //puts no entry tiles on islands
                int j = 1;
                for (IslandPojo island : game.getIslands()) {
                    for (int i = 0; i < island.getNumNoEntryTile(); i++) {
                        anchorPane = (AnchorPane) currentStage.getScene().lookup("#island" + (j));
                        gridPane = (GridPane) anchorPane.getChildren().get(1);
                        imageView = new ImageView(new Image("/images/imageCharacters/deny_island_icon.png"));
                        imageView.setFitWidth(30.0);
                        imageView.setPreserveRatio(true);
                        gridPane.add(imageView, 6, 6 - i);
                    }
                    j++;
                }
            }

            // put pawns on  card
            if (card.getCharacterId() == 1 || card.getCharacterId() == 11  || card.getCharacterId() == 7) {
                gridPane = (GridPane) anchorPane.getChildren().get(1);
                List<ImageView> pawnsList = PawnsToImageStudents(card.getStudents());
                rig = 0;
                col = 0;
                for (int i = 0; i < pawnsList.size(); i++) {
                    if (col == 3) {
                        col = 0;
                        rig = 1;
                    }
                    imageView= pawnsList.get(i);
                    imageView.setFitWidth(30.0);
                    imageView.setPreserveRatio(true);
                    gridPane.add(imageView, col, rig);
                    col++;
                }
            }



        }
    }


    /** reset character cards view (elements on the card) in order to show the right elemnts after
     * updateCharacterCards().
     * It clears the grids on the card.
     * */
    private void resetCharacterCards() {
        GameStatePojo game = ClientController.getInstance().getGameStatePojo();
        AnchorPane anchorPane;
        GridPane gridPane;
        ImageView imageView;

        for (CharacterPojo card : game.getCharacters()) {
            anchorPane = (AnchorPane) currentStage.getScene().lookup("#card" + card.getCharacterId());
            gridPane = (GridPane) anchorPane.getChildren().get(1);
            gridPane.getChildren().clear();
        }
    }
    /**used by card 3 to show the alert with card descritption and change cursor during card's effect*/
    public void activeGuiCard3() {

        showBanner(3);

        changeCursor(2); // calculate influence cursor

    }
    /**used by card 3 to show the alert with card descritption and change cursor during card's effect*/
    public void activeGuiCard5() {
        showBanner(5);

        changeCursor(3); //no entry tile

    }

    /**
     * Adds the MouseEvent on the card to enable drags of the strudents on the cards
     */
    public void activeGuiCard1() {
        showBanner(1);

        AnchorPane anchorPane;
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#card1");

        GridPane gridPane = (GridPane) anchorPane.getChildren().get(1);
        ImageView imageView;
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            imageView = (ImageView) gridPane.getChildren().get(i);
            imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    GameHandlerScene.dragStudentForCard(event);
                }
            });
        }
    }

    /**
     * Adds the MouseEvent on the card to enable click of the strudents on the cards
     */
    public void activeGuiCard11() {

        showBanner(11);

        AnchorPane anchorPane;
        GridPane gridPane;
        ImageView imageView;


        anchorPane = (AnchorPane) currentStage.getScene().lookup("#card11");
        gridPane = (GridPane) anchorPane.getChildren().get(1);
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            imageView = (ImageView) gridPane.getChildren().get(i);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    GameHandlerScene.setColourChosen(event);
                }
            });
        }

    }
    /**method used by all cards(also those that don't need any action by the player)
     * when card is activated this banner is shown,
     * cards 9, 12, 7, 10 have a different own banner*/
    public static void showBanner(int num){
        Parent root = null;
        try {
            root = FXMLLoader.load(GuiController.class.getClassLoader().getResource("cardsDialog.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Stage alert = new Stage();
        Scene scene = new Scene(root);
        alert.setScene(scene);
        alert.setResizable(false);
        alert.sizeToScene();
        alert.setOnCloseRequest(event -> event.consume() );
        alert.getIcons().add(new Image("/images/imageCharacters/Moneta_base.png"));
        alert.initOwner(currentStage);
        alert.initModality(Modality.APPLICATION_MODAL);

        CharacterPojo characterPojo = ClientController.getInstance().getGameStatePojo()
                .getCharacters().stream().filter(a->a.getCharacterId()==num).collect(Collectors.toList()).get(0);
        DialogPane dialogPane = (DialogPane)root;

        ((Text)dialogPane.getHeader()).setText("CARD "+num);
        ((Text) alert.getScene().lookup("#description")).setText("\nWith this cards you get a magic power!\n"+characterPojo.getDescription());
        Button button = (Button) ((AnchorPane) ((DialogPane)root).getContent()).getChildren().get(1);
        button.setOnMouseClicked((event)->alert.close());

        alert.showAndWait();

    }


    /** method used by cards 9 and 12: alert with selection.
     * Adds the MouseEvent on the card to enable click of the students on the cards
     *  */
    public void activeGuiCard9_12(int num){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("cardsDialogColour.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Stage alert = new Stage();
        Scene scene = new Scene(root);
        alert.setScene(scene);
        alert.setResizable(false);
        alert.sizeToScene();
        alert.setOnCloseRequest(event -> event.consume() );
        alert.getIcons().add(new Image("/images/imageCharacters/Moneta_base.png"));
        alert.initOwner(currentStage);
        //alert.initModality(Modality.APPLICATION_MODAL);


        CharacterPojo characterPojo = ClientController.getInstance().getGameStatePojo()
                .getCharacters().stream().filter(a->a.getCharacterId()==num).collect(Collectors.toList()).get(0);
        DialogPane dialogPane = (DialogPane)root;

        ((Text)dialogPane.getHeader()).setText("CARD "+num);
        ((Text) alert.getScene().lookup("#description")).setText("\nWith this cards you get a magic power!\n"+characterPojo.getDescription());
        int i =0;
        for(ImageView image : ( ((AnchorPane) alert.getScene().lookup("#colours")).getChildren()).stream().map(a->(ImageView)a).collect(Collectors.toList())){
            image.setUserData(i);
            image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    ClientController.getInstance().getCharacterCardsConsole().setPawnColour(((Integer)((ImageView) event.getTarget()).getUserData()));
                    ClientController.getSemaphore().release();
                    alert.close();
                }
            });
            i++;
        }

        alert.show();
    }

    /** method used by cards 7 and 10: alert with selection.
     * Adds the MouseEvent on the button to enable choosing pawns to move.
     *  */
    public void activeGuiCard7_10(int num){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("cardsDialogNumber.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Stage alert = new Stage();
        Scene scene = new Scene(root);
        alert.setScene(scene);
        alert.setResizable(false);
        alert.sizeToScene();
        alert.setOnCloseRequest(event -> event.consume() );
        alert.getIcons().add(new Image("/images/imageCharacters/Moneta_base.png"));
        alert.initOwner(currentStage);
        //alert.initModality(Modality.APPLICATION_MODAL);

        CharacterPojo characterPojo = ClientController.getInstance().getGameStatePojo()
                .getCharacters().stream().filter(a->a.getCharacterId()==num).collect(Collectors.toList()).get(0);
        DialogPane dialogPane = (DialogPane)root;

        ((Text)dialogPane.getHeader()).setText("CARD "+num);
        ((Text) alert.getScene().lookup("#description")).setText("\nWith this cards you get a magic power!\n"+characterPojo.getDescription());

        Button button = (Button) ((AnchorPane) ((DialogPane)root).getContent()).getChildren().get(2);
        Spinner spinner = (Spinner) ((AnchorPane) ((DialogPane)root).getContent()).getChildren().get(1);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, num==7 ? 3 : 2));

        Consumer<Boolean> consumerInvalidChoise = (ok) -> {
            Platform.runLater(() -> {
                if (!ok) {
                    alert.close();
                }
            });
        };
        ClientController.getInstance().getView().setInvalidChoiseObserver(consumerInvalidChoise);
        Consumer<Boolean> correctMoveConsumer = (ok)->{
            Platform.runLater(() -> {
                if (ok) {
                    alert.close();
                }
            });

        };
        ClientController.getInstance().getView().setCorrectMoveObserver(correctMoveConsumer);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                AnchorPane father = (AnchorPane) button.getParent();
                int val = (Integer) ((Spinner) father.getChildren().get(1)).getValue();
                ClientController.getInstance().getCharacterCardsConsole().setPawnsToMove(val);
                ClientController.getSemaphore().release();
            }
        });

        alert.show();

    }

    /**
     * method used by card 10.
     * Adds the ClickMouseEvent on the students in entrance and on the students
     * in dining room, in order to fires the event when the card is active and the player clicks on
     * a student
     */
    public static void addClickCard10(){
        //add click on students in entrance
        int numPlayers = ClientController.getInstance().getGameStatePojo().getPlayers().size();
        int i;
        AnchorPane anchorPane;
        ImageView image;
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#entrance" + GameHandlerScene.getMyOrderInPlayers());
        for (AnchorPane child : anchorPane.getChildren().stream().map(a -> (AnchorPane) a).collect(Collectors.toList())) {
            if (child.getChildren().size() > 0) {
                image = (ImageView) child.getChildren().get(0);
                image.setOnMouseClicked((event) -> GameHandlerScene.clickStudentEntrance(event));
            }
        }

        GridPane gridPane;
        //add click in dining
        gridPane = (GridPane) currentStage.getScene().lookup("#studentsPlancia" + GameHandlerScene.getMyOrderInPlayers());
        for (ImageView student : gridPane.getChildren().stream().map(a -> (ImageView) a).collect(Collectors.toList())) {
            student.setOnMouseClicked((event) -> GameHandlerScene.clickStudentDining(event));
        }

    }

    /**
     * method used by card 7.
     * Adds the ClickMouseEvent on the students in entrance and on the students
     * on the card, in order to fires the event when the card is active and the player clicks on
     * a student
     */
    public static void addClickCard7(){
        //add click on students in entrance
        int numPlayers = ClientController.getInstance().getGameStatePojo().getPlayers().size();
        int i;
        AnchorPane anchorPane;
        ImageView image;
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#entrance" + GameHandlerScene.getMyOrderInPlayers());
        for (AnchorPane child : anchorPane.getChildren().stream().map(a -> (AnchorPane) a).collect(Collectors.toList())) {
            if (child.getChildren().size() > 0) {
                image = (ImageView) child.getChildren().get(0);
                image.setOnMouseClicked((event) -> GameHandlerScene.clickStudentEntrance(event));
            }
        }
        GridPane gridPane;

        //add click on card
        anchorPane = (AnchorPane) currentStage.getScene().lookup("#card7");
        gridPane = (GridPane) anchorPane.getChildren().get(1);
        for (i = 0; i < gridPane.getChildren().size(); i++) {
            image = (ImageView) gridPane.getChildren().get(i);
            image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    GameHandlerScene.setColourChosen(event);
                }
            });
        }

    }
    /** open new scene with character cards description*/
    public static void openCardsDescription(){
        Parent root = null;
        try {
            root = FXMLLoader.load(GuiController.class.getClassLoader().getResource("cardsDescription.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Stage banner = new Stage();
        banner.setTitle("Cards");
        Scene scene = new Scene(root);
        banner.setScene(scene);
        banner.setResizable(false);
        banner.sizeToScene();
        banner.getIcons().add(new Image("/images/imageCharacters/Moneta_base.png"));
        banner.show();
    }


    public void closeGame() {
        if(otherPlayerDisconnected) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Game ended: other player disconnected", ButtonType.OK);
            alert.setOnCloseRequest(event -> event.consume() );
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.exit(0);
                }
            });
        }else if(pingUnreceived = true){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Connection to the server lost", ButtonType.OK);
            alert.setOnCloseRequest(event -> event.consume() );
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.exit(0);
                }
            });
        }else{
            System.exit(0);
        }
    }
}

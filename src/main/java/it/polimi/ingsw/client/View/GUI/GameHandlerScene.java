package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.Console;
import it.polimi.ingsw.common.gamePojo.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * Class that responses to the click event on a game object, all the methods verify if the action is valid and unlock
 * the game if the choose is ok otherwise no action is made
 */
public class GameHandlerScene {

    private static ColourPawn colourStudent=null;

    private static int myOrderInPlayers;
    //setted by ask for character to notify the user has already decided to use or not to use a character card
    private static boolean cardToUse = false;

    private static Stage currentStage;

    private static String dragged;

    private static boolean moveStudentCard;

    private static boolean chooseIsland;
    private static boolean pawnColourBoolean;
    private static boolean clickEntrance;
    private static boolean clickDining;
    private static boolean secondPart;
    private static boolean canMoveCoin;
    private static boolean skipAskForCharacter;
    private static boolean motherNatureError;

    public static void skipAskForForCharacter(boolean b) {
        skipAskForCharacter=b;
    }

    /**set that player has chosen cloud 0*/
    @FXML
    void setCloudChosen1(MouseEvent event) {

        if (correctAction(Console.ActionBookMark.chooseCloud) && !cardToUse) {
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true && !skipAskForCharacter) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }

            ClientController.getInstance().getConsole().setCloudChosen(0);
            ClientController.getSemaphore().release();
            skipAskForCharacter=false;
        }
    }

    /**set that player has chosen cloud 1*/
    @FXML
    void setCloudChosen2(MouseEvent event) {
        if (correctAction(Console.ActionBookMark.chooseCloud) && !cardToUse) {
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true && !skipAskForCharacter) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }

            ClientController.getInstance().getConsole().setCloudChosen(1);
            ClientController.getSemaphore().release();
            skipAskForCharacter=false;
        }
    }

    /**set that player has chosen cloud 2*/
    @FXML
    void setCloudChosen3(MouseEvent event) {
        if (correctAction(Console.ActionBookMark.chooseCloud) && !cardToUse) {

            if (ClientController.getInstance().getGameStatePojo().isExpert() == true && !skipAskForCharacter) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }
            ClientController.getInstance().getConsole().setCloudChosen(2);
            ClientController.getSemaphore().release();
            skipAskForCharacter=false;
        }
    }

    /** method that detects that the player has dragged a student from entrance and set the color of the student on the console**/
    public static void  setStudentChosen(MouseEvent event) {

        // if isn't my turn and not in moveStudents Phase: no action
        if(correctAction(Console.ActionBookMark.moveStudents) && !cardToUse) {
            //drag
            ImageView imageView = (ImageView)event.getTarget();
            //control if the drag starts from correct player's schoolBoard (maybe a player drag the student of an other player)
            if(((AnchorPane)imageView.getParent().getParent()).getId().equals("entrance"+myOrderInPlayers)) {
                colourStudent = (ColourPawn) imageView.getUserData(); // save value and return
                //show student shadow in movement
                Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(imageView.getImage());
                db.setContent(content);
                dragged="student";
            }
        }

        event.consume();

    }

    /**
     * to accept the drop according to the dragged element while moving the element on the board
     * @param event
     */
    @FXML
    void acceptDropMoveStudent(DragEvent event){
        if(dragged.equals("student")) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        //** this method is used by card 1 for the drop
        else if(moveStudentCard && dragged.equals("studentCard")) {
            AnchorPane anchorPane = (AnchorPane) event.getSource();
            String id= anchorPane.getId();
            if(id.substring(0,6).equals("island")) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        }
    }

    /**
     * to drop the student on the game board
     * @param event
     */
    @FXML
    void setStudentOnGameBoard(DragEvent event) {
        if(correctAction(Console.ActionBookMark.moveStudents) && !cardToUse) {
            //if complex mode before moving the student the server watnts to know if the player wants to use
            // a character card (in CLI y/n ), in this case the player doesn't want to use a character card - > n
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true && !skipAskForCharacter) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }
            ClientController.getInstance().getConsole().setPawnColour(colourStudent.getIndexColour());
            ClientController.getInstance().getConsole().setPawnWhere(-1);
            ClientController.getSemaphore().release();
            skipAskForCharacter=false;
        }
        event.setDropCompleted(true);
        event.consume();
    }

    /**
     * the player drop a pawn on an island. It is used:
     * ---> in the basic game mode
     * ---> in complex mode, if moveStudentCard == true: card 1 use this to choose an island
     * @param event
     */
    @FXML
    void setStudentOnIsland(DragEvent event) {
        if(correctAction(Console.ActionBookMark.moveStudents) && !cardToUse) {
            //if complex mode before moving the student the server watnts to know if the player wants to use
            // a character card (in CLI y/n ), in this case the player doesn't want to use a character card - > n
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true && !skipAskForCharacter) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }

            AnchorPane anchorPaneClicked = (AnchorPane) event.getSource();
            String islandIdString;
            int islandId;
            islandIdString= anchorPaneClicked.getId().substring(6);
            islandId= Integer.parseInt(islandIdString);
            ClientController.getInstance().getConsole().setPawnColour(colourStudent.getIndexColour());
            ClientController.getInstance().getConsole().setPawnWhere(islandId-1);
            ClientController.getSemaphore().release();
            skipAskForCharacter=false;
        }
        //** used by card 1 **//
        else if(moveStudentCard){
            acceptDropStudentForCard(event);
        }
        event.setDropCompleted(true);
        event.consume();
    }


    /**
     * the player clicks on an island:
     * ---> in the basic game mode it is interpreted as the place where to put mother nature
     * ---> in complex mode, if chooseIsland == true: card 3 and 5 use this to choose an island
     * @param event
     */
    @FXML
    void setMotherNatureToIsalnd(MouseEvent event) {
        if (correctAction(Console.ActionBookMark.placeMotherNature) && !cardToUse) {
            //if complex mode before moving the student the server watnts to know if the player wants to use
            // a character card (in CLI y/n ), in this case the player doesn't want to use a character card - > n
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true && !skipAskForCharacter && !motherNatureError) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }
            //call back for error in movements
            Runnable runnableInvalidChoise = () -> {
                Alert alert = new Alert(Alert.AlertType.ERROR,"not so many steps ! " , ButtonType.OK);
                alert.showAndWait();
            };

            AnchorPane anchorPaneClicked = (AnchorPane) event.getSource();
            String islandIdString;
            int islandId;

            islandIdString = anchorPaneClicked.getId().substring(6);
            islandId = Integer.parseInt(islandIdString);
            int result;
            int posMotherNature = 0;
            //find mother nature
            for (int pos = 0; pos < ClientController.getInstance().getGameStatePojo().getIslands().size(); pos++) {
                if (ClientController.getInstance().getGameStatePojo().getIslands().get(pos).isHasMotherNature()) {
                    posMotherNature = pos + 1;
                }
            }
            if (posMotherNature <= islandId) {
                result = islandId - posMotherNature;
            } else {
                result = ClientController.getInstance().getGameStatePojo().getIslands().size() - posMotherNature + islandId;
            }
            int moreSteps=0;
            if(ClientController.getInstance().getGameStatePojo().getActiveEffect()!=null){
                if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==4)
                    moreSteps=2;
            }
            if(result==0 || result > ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getPlayedAssistantCard().getMovementsMotherNature()+moreSteps){
                GuiController.getInstance().setRunnable(runnableInvalidChoise);
                GuiController.getInstance().runMethod();
                motherNatureError=true;
                event.consume();
            }else {
                ClientController.getInstance().getConsole().setStepsMotherNature(result);
                ClientController.getSemaphore().release();
                skipAskForCharacter = false;
                motherNatureError=false;
            }
        }

        //** this method is used by card 3 and 5 **//
        else if(chooseIsland){
            setIslandChosenForCard(event);
        }

    }

    /**receives in input an action bookmark (in Console class) and return true if is my turn, the game is in action phase
     * and the action phase is in right action bookmark in input
     */
    private static boolean correctAction(Console.ActionBookMark actionBookMark){
        return ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.ACTION &&
                ClientController.getInstance().getConsole().getCurrActionBookMark() == actionBookMark;
    }

    /**sets the player's order from player list: used for detecting the school board when an event is fired */
    public static void setMyOrderInPlayers(){
        int i=0;
        for(PlayerPojo player: ClientController.getInstance().getGameStatePojo().getPlayers()){
            if (ClientController.getInstance().getNickname().equals(player.getNickname())){
                break;
            }
            i++;
        }
        myOrderInPlayers = (i+1);
    }

    public static int getMyOrderInPlayers(){
        return myOrderInPlayers;
    }


    /**changes view of player's coins tab if in complex mode when tab button clickd*/
    @FXML
    void showDetailsPlancia(Event event) {
        Tab tab = (Tab )event.getTarget();
        if(tab.getId().equals("Plancia1")){
            GuiController.getInstance().setRunnable(()-> GuiController.getInstance().activeDetails(1));
            GuiController.getInstance().runMethod();
        }else if(tab.getId().equals("Plancia2")){
            GuiController.getInstance().setRunnable(()-> GuiController.getInstance().activeDetails(2));
            GuiController.getInstance().runMethod();
        }else if(tab.getId().equals("Plancia3")){
            GuiController.getInstance().setRunnable(()-> GuiController.getInstance().activeDetails(3));
            GuiController.getInstance().runMethod();
        }
    }

    public static void setCharacterCardToUse(boolean use){
        cardToUse=use;
    }


    @FXML
    void setAssistantCardChosen1(MouseEvent event) {
        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(1);
            ClientController.getSemaphore().release();
        }


    }

    @FXML
    void setAssistantCardChosen2(MouseEvent event) {
        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(2);
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen3(MouseEvent event) {
        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(3);
            ClientController.getSemaphore().release();
        }
    }
    @FXML
    void setAssistantCardChosen4(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(4);
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen5(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(5);
            ClientController.getSemaphore().release();
        }
    }
    @FXML
    void setAssistantCardChosen6(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(6);
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen7(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(7);
            ClientController.getSemaphore().release();
        }
    }
    @FXML
    void setAssistantCardChosen8(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(8);
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen9(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(9);
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen10(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(10);
            ClientController.getSemaphore().release();
        }
    }

    public static void setStage(Stage stage){
        currentStage= stage;
    }

    //////////------------------------/////////////--------------//////////
    //                  COMPLEX MODE METHODS:                            //
    //////////------------------------/////////////--------------//////////


    /**
     * Sets the consumers setNoEnoughCoinsObserver and setNoEnoughCoinsObserver in the guiView.
     * In case a NoMoney or a InvalidChoice message is received (after the player's attempt to use a character-effect),
     * the GuiView consumes them.
     * They show an alert message on the screen.
     */
    private static void setObserversErrors(){
        Consumer<Boolean> consumerCoins = (ok) -> {
            Platform.runLater(() -> {
                if (!ok) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No enough coins", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        };
        ClientController.getInstance().getView().setNoEnoughCoinsObserver(consumerCoins);

        Consumer<Boolean> consumerInvalidChoise = (ok) -> {
            Platform.runLater(() -> {
                if (!ok) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Card already used" , ButtonType.OK);
                    alert.showAndWait();
                }
            });
        };
        ClientController.getInstance().getView().setInvalidChoiseObserver(consumerInvalidChoise);
    }





    /**
     * When the player clicks the coin,
     * if it is a coin of his own and not an other player's coin,
     * the Drag and Drop begins
     */
    @FXML
    public void useCoins(MouseEvent mouseEvent) {
        if(ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname())
                && ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.ACTION && !cardToUse ) {
            //verify if drag starts from correct players's school board
            int index = ((TabPane) currentStage.getScene().lookup("#boards")).getSelectionModel().getSelectedIndex() +1 ;

            if (index == myOrderInPlayers) {
                //set image
                ImageView imageView = (ImageView) mouseEvent.getTarget();
                Dragboard db = imageView.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putImage(imageView.getImage());
                db.setContent(content);
                dragged="coins";
                setObserversErrors();
            }
            mouseEvent.consume();
        }
    }

    /**
     * the Drag and Drop started by useCoins-method ends
     * The drop is accepted only if the item dropped is a coin
     * @param event
     */
    @FXML
    public void acceptDropUseCoins(DragEvent event){
        if(dragged.equals("coins") && canMoveCoin) {
            event.acceptTransferModes(TransferMode.COPY);
        }
    }

    public static void canMoveCoin(boolean b) {
        canMoveCoin=b;
    }


    /**
     * Waits that the ClientController reads the message sent by the server. If it is an error messsage, an alert will
     * be shown (thanks to setObserversErrors method)
     * @param event
     */
    @FXML
    public void tryUseCard(DragEvent event){


        String id = ((AnchorPane) event.getSource()).getId().substring(4);
        ClientController.getInstance().getConsole().setCharacterPlayed(Integer.parseInt(id));
        ClientController.getSemaphore().release();

        event.setDropCompleted(true);
        event.consume();

    }




    /////////////////////////    ///////////////////     CARD 1    ///////////////////     /////////////////////////

    public static void setMoveStudentCard(boolean moveStudentCard) {
        GameHandlerScene.moveStudentCard = moveStudentCard;
    }



    /**
     * Card 1 uses this whan the payer has to drags a pawn from the card:
     * this method is used by cards 1 to start the drag event,
     * as done for CLI, this method is enabled by moveStudentToIsland in GUIView
     */
    public static void dragStudentForCard(MouseEvent event){
        if(moveStudentCard) {
            dragged = "studentCard"; //avoid user can put the element in other areas that accept drop (e.g. schoolBoard)
            ImageView imageView = (ImageView)event.getTarget();
            colourStudent = (ColourPawn) imageView.getUserData();
            Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(imageView.getImage());
            db.setContent(content);
        }

    }


    /**
     * Card 1 uses this whan the payer has to drop a pawn on the island
     */
    private void acceptDropStudentForCard(DragEvent event){
        String islandIdString;
        int islandId;
        AnchorPane anchorPane = (AnchorPane) event.getSource();
        islandIdString= anchorPane.getId().substring(6);


        islandId= Integer.parseInt(islandIdString);
        ClientController.getInstance().getCharacterCardsConsole().setPawnColour(colourStudent.getIndexColour());
        ClientController.getInstance().getCharacterCardsConsole().setPawnWhere(islandId-1);
        ClientController.getSemaphore().release();

    }


    /////////////////////////    ///////////////////     CARD 3 and 5    ///////////////////     /////////////////////////



    public static void setChooseIsland(boolean chooseIsland) {
        GameHandlerScene.chooseIsland = chooseIsland;
    }


    /**
     * this method is used by cards 3 and 5 for clicking an island and set the chosen island,
     * as done for CLI, this method is enabled by chooseIsland() in GUIView and set the chosen value in
     * CharacterCardsConsole's attribute : pawnWhere
     * The player choose an island and cliks on it.
     * (The methos chooseIsland of the GUIview calls activeGuiCard3/5 in the GUIController and
     * activeGuiCard3/5 activates on the islands a eventHandler to handle this method.)
     */
     private void setIslandChosenForCard(MouseEvent event){

         AnchorPane anchorPaneClicked = (AnchorPane) event.getSource();
         String islandIdString;
         int islandId;

         islandIdString = anchorPaneClicked.getId().substring(6);
         islandId = Integer.parseInt(islandIdString);

         ClientController.getInstance().getCharacterCardsConsole().setPawnWhere(islandId-1);
         ClientController.getSemaphore().release();


    }


    /////////////////////////    ///////////////////     CARD 11 & 7  ///////////////////     /////////////////////////

    public static void setPawnColourBoolean(boolean pawnColourBoolean) {
        GameHandlerScene.pawnColourBoolean = pawnColourBoolean;
    }



    /**
     * Card 11 uses this whan the payer has to choose a pawn on the card
     */
    @FXML
    public static void setColourChosen(MouseEvent event){

        if (pawnColourBoolean) {
            ImageView imageView = (ImageView) event.getSource();
            ColourPawn colourStudent = (ColourPawn) imageView.getUserData();

            ClientController.getInstance().getCharacterCardsConsole().setPawnColour(colourStudent.getIndexColour());
            ClientController.getSemaphore().release();

            secondPart=false;
        }
    }



    /////////////////////////    ///////////////////     CARD 10   ///////////////////     /////////////////////////

    /** enables click on student in entrance */
    public static void setClickStudentEntrance(boolean enable){
        clickEntrance = enable;
    }

    /**enables click for second item to click */
    public static boolean isSecondPart() {
        return secondPart;
    }
    /**enable click on students in dining for this card effect*/
    public static void setClickDining(boolean clickDining) {
        GameHandlerScene.clickDining = clickDining;
    }

    /**used by cards 7 and 10: when clickEntrance is true it enables the click on the students in entrance*/
    public static void clickStudentEntrance(MouseEvent event) {
        if (clickEntrance) {

            ImageView image = (ImageView) event.getTarget();
            ClientController.getInstance().getCharacterCardsConsole().setPawnColour(((ColourPawn) image.getUserData()).getIndexColour());
            ClientController.getSemaphore().release();
            clickEntrance = false;
            secondPart = true;


            MotionBlur mb = new MotionBlur();
            mb.setRadius(10.0f);
            image.setEffect(mb);
        }
    }

    /**used by cards 10: when clickEntrance is true it enables the click on the students in dining room*/
    public static void clickStudentDining(MouseEvent event) {
        if (clickDining) {
            ImageView image = (ImageView) event.getTarget();
            ClientController.getInstance().getCharacterCardsConsole().setPawnColour(GridPane.getRowIndex(image));
            ClientController.getSemaphore().release();
            clickDining = false;
            secondPart=false;
        }
    }
    /** open new scene with character cards description*/
    @FXML
    void openCardsDescritpion(MouseEvent event){
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().openCardsDescription());
        GuiController.getInstance().runMethod();
    }

}





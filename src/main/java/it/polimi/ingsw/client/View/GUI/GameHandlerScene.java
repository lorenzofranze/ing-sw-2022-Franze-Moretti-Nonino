package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.Console;
import it.polimi.ingsw.common.gamePojo.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**class that responses to the click event on a game object, all the methods verify if the action is valid and unlock
 * the game if the choose is ok otherwise no action
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


    @FXML
    void setCloudChosen1(MouseEvent event) {

        if (correctAction(Console.ActionBookMark.chooseCloud) && !cardToUse) {
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }

            ClientController.getInstance().getConsole().setCloudChosen(0);
            ClientController.getSemaphore().release();
        }
    }


    @FXML
    void setCloudChosen2(MouseEvent event) {
        if (correctAction(Console.ActionBookMark.chooseCloud) && !cardToUse) {
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }

            ClientController.getInstance().getConsole().setCloudChosen(1);
            ClientController.getSemaphore().release();
        }
    }


    @FXML
    void setCloudChosen3(MouseEvent event) {
        if (correctAction(Console.ActionBookMark.chooseCloud) && !cardToUse) {

            if (ClientController.getInstance().getGameStatePojo().isExpert() == true) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }
            ClientController.getInstance().getConsole().setCloudChosen(1);
            ClientController.getSemaphore().release();
        }
    }

    /** method that detecst that the player has dragged a student from entry and set the color of the student on the console**/
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

    @FXML
    void acceptDropMoveStudent(DragEvent event){
        if(dragged.equals("student")) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        else if(dragged.equals("studentCard")) {
            System.out.println("flag1 gamehandlerscene");
            AnchorPane anchorPane = (AnchorPane) event.getSource();
            String id= anchorPane.getId();
            if(id.substring(0,6).equals("island")){
                event.acceptTransferModes(TransferMode.MOVE);
                System.out.println("flag2 gamehandlerscene");
            }
        }
    }

    @FXML
    void setStudentOnGameBoard(DragEvent event) {
        if(correctAction(Console.ActionBookMark.moveStudents) && !cardToUse) {
            //if complex mode before moving the student the server watnts to know if the player wants to use
            // a character card (in CLI y/n ), in this case the player doesn't want to use a character card - > n
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }
            ClientController.getInstance().getConsole().setPawnColour(colourStudent.getIndexColour());
            ClientController.getInstance().getConsole().setPawnWhere(-1);
            ClientController.getSemaphore().release();
        }
        event.setDropCompleted(true);
        event.consume();
    }

    @FXML
    void setStudentOnIsland(DragEvent event) {
        if(correctAction(Console.ActionBookMark.moveStudents) && !cardToUse) {
            //if complex mode before moving the student the server watnts to know if the player wants to use
            // a character card (in CLI y/n ), in this case the player doesn't want to use a character card - > n
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }

            AnchorPane anchorPaneClicked = (AnchorPane) event.getSource();
            String islandIdString;
            int islandId;
            islandIdString= anchorPaneClicked.getId().substring(6,7);
            islandId= Integer.parseInt(islandIdString);
            ClientController.getInstance().getConsole().setPawnColour(colourStudent.getIndexColour());
            ClientController.getInstance().getConsole().setPawnWhere(islandId-1);
            ClientController.getSemaphore().release();
        }
        else if(pawnColourBoolean){
            acceptDropStudentForCard(event);
        }
        event.setDropCompleted(true);
        event.consume();
    }


    @FXML
    void setMotherNatureToIsalnd(MouseEvent event) {
        if (correctAction(Console.ActionBookMark.placeMotherNature) && !cardToUse) {
            //if complex mode before moving the student the server watnts to know if the player wants to use
            // a character card (in CLI y/n ), in this case the player doesn't want to use a character card - > n
            if (ClientController.getInstance().getGameStatePojo().isExpert() == true) {
                ClientController.getInstance().getConsole().setCharacterPlayed(null);
                ClientController.getSemaphore().release();
            }

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
            ClientController.getInstance().getConsole().setStepsMotherNature(result);
            ClientController.getSemaphore().release();
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
     * if it it a coin of his own and not an other player's coin,
     * the Drag and Drop begins
     */
    @FXML
    public void useCoins(MouseEvent mouseEvent) {
        if(ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname())
                && ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.ACTION && !cardToUse) {
            //verify if drag starts from correct players's school board
            int index = ((TabPane) currentStage.getScene().lookup("#boards")).getSelectionModel().getSelectedIndex() +1 ;

            if (index == myOrderInPlayers) {
                //set image
                ImageView imageView = (ImageView) mouseEvent.getTarget();
                Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(imageView.getImage());
                db.setContent(content);
                dragged="coins";
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
        if(dragged.equals("coins")) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
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
        System.out.println("GameHandlerScene: using coin for character "+ id);
        ClientController.getSemaphore().release();
        setObserversErrors();
        System.out.println("drop rilevato");
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
        event.consume();

    }


    /**
     * Card 1 uses this whan the payer has to drop a pawn on the island
     */

    private void acceptDropStudentForCard(DragEvent event){
        if(dragged.equals("studentCard")) {
            event.acceptTransferModes(TransferMode.MOVE);
            AnchorPane anchorPaneClicked = (AnchorPane) event.getSource();
            String islandIdString;
            int islandId;
            islandIdString= anchorPaneClicked.getId().substring(6);
            islandId= Integer.parseInt(islandIdString);
            ClientController.getInstance().getConsole().setPawnColour(colourStudent.getIndexColour());
            ClientController.getInstance().getConsole().setPawnWhere(islandId-1);
            ClientController.getSemaphore().release();
        }
        event.setDropCompleted(true);
        event.consume();


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

         System.out.println("cliccato l'isola");
         AnchorPane anchorPaneClicked = (AnchorPane) event.getSource();
         String islandIdString;
         int islandId;

         islandIdString = anchorPaneClicked.getId().substring(6);
         islandId = Integer.parseInt(islandIdString);
         System.out.println("cliccato l'isola numero "+islandIdString);

         ClientController.getInstance().getCharacterCardsConsole().setPawnWhere(islandId-1);
         ClientController.getSemaphore().release();


    }


    /////////////////////////    ///////////////////     CARD 11    ///////////////////     /////////////////////////

    public static void setPawnColourBoolean(boolean chooseIsland) {
        GameHandlerScene.pawnColourBoolean = pawnColourBoolean;
    }



    /**
     * Card 11 uses this whan the payer has to choose a pawn on the card
     */
    @FXML
    public static void setColourChosen(MouseEvent event){
        System.out.println("flag2");
        if (pawnColourBoolean) {

            ImageView imageView = (ImageView) event.getTarget();
            ColourPawn colourStudent = (ColourPawn) imageView.getUserData();

            ClientController.getInstance().getCharacterCardsConsole().setPawnColour(colourStudent.getIndexColour());
            ClientController.getSemaphore().release();
        }
    }



    /////////////////////////    ///////////////////     CARD 10   ///////////////////     /////////////////////////

    /** enables click on student in entrance */
    public static void enableClickStudentEntranceForCard(boolean enable){
        clickEntrance = enable;
    }


    public static void clickStudentEntrance(MouseEvent event) {
        if (clickEntrance) {
            ImageView image = (ImageView) event.getTarget();
            ClientController.getInstance().getCharacterCardsConsole().setPawnColour(((ColourPawn) image.getUserData()).getIndexColour());
            ClientController.getSemaphore().release();
            clickEntrance = false;
            clickDining = true;
        }
    }

    public static void clickStudentDining(MouseEvent event) {
        if (clickDining) {
            ImageView image = (ImageView) event.getTarget();
            ClientController.getInstance().getCharacterCardsConsole().setPawnColour(GridPane.getColumnIndex(image));
            ClientController.getSemaphore().release();
            clickDining = false;
            System.out.println(GridPane.getColumnIndex(image));
        }
    }

}





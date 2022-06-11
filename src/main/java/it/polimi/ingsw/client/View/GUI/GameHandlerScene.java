package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.Console;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**class that responses to the click event on a game object, all the methods verify if the action is valid and unlock
 * the game if the choose is ok otherwise no action
 */
public class GameHandlerScene {

    private static ColourPawn colourStudent;

    private static int myOrderInPlayers;
    //setted by ask for character to notify the user has already decided to use or not to use a character card
    private static boolean cardToUse = false;

    private static String coinsId;


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
            }
        }
        event.consume();

    }

    @FXML
    void acceptDrop(DragEvent event){
        event.acceptTransferModes(TransferMode.MOVE);
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
            islandIdString= anchorPaneClicked.getId().substring(6);
            islandId= Integer.parseInt(islandIdString);
            ClientController.getInstance().getConsole().setPawnColour(colourStudent.getIndexColour());
            ClientController.getInstance().getConsole().setPawnWhere(islandId-1);
            ClientController.getSemaphore().release();
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
        myOrderInPlayers = i+1;
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

    public static void setCharacterCardPlayable(){
        cardToUse=false;
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
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Card already used", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        };
        ClientController.getInstance().getView().setNoEnoughCoinsObserver(consumerInvalidChoise);
    }

    public static void tryUseCard9(DragEvent event){
        ClientController.getInstance().getConsole().setCharacterPlayed(9);
        ClientController.getSemaphore().release();

        setObserversErrors();

        event.setDropCompleted(true);
        event.consume();

    }


    public void useCoins(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getTarget();
        coinsId = ((AnchorPane)imageView.getParent()).getId();
        Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        db.setContent(content);
        mouseEvent.consume();
    }


}

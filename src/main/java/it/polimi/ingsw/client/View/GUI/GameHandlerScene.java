package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**class that responses to the click event on a game object, all the methods verify if the action is valid and unlock
 * the game if the choose is ok otherwise no action
 */
public class GameHandlerScene {
    //** todo scelta di mettere studenti su isola, scelta spostamento madre natura, scelta carte personaggio
    //** todo impedire mosse sbagliate aggiungendo negli if(ACTION && ...)
    //** todo update consecutivi si sovrappongono? o creano ritardo?

    //set to true after mother nature step choice, set to false during the pianification
    private boolean isCloudTurn=false;

    //set to true after assistantcard choice, set to false during the choice of the clouds
    private boolean isStudentTurn=true;



    @FXML
    void setCloudChosen1(MouseEvent event) {
        if(ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.ACTION){
            ClientController.getInstance().getConsole().setCloudChosen(0);
        }

    }

    @FXML
    void setCloudChosen2(MouseEvent event) {
        if(ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.ACTION){
            ClientController.getInstance().getConsole().setCloudChosen(1);
        }

    }


    @FXML
    void setCloudChosen3(MouseEvent event) {
        if(ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.ACTION){
            ClientController.getInstance().getConsole().setCloudChosen(2);
        }

    }


    /** method that detecst that the player has dragged a student from entry and set the color of the student on the console**/
    @FXML
    void setStudentChosen(MouseEvent event) {
        if(ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.ACTION) {
            AnchorPane anchorPaneClicked = (AnchorPane)event.getSource();
            if(anchorPaneClicked.getChildren().get(0)==null) return;
            else{
                ImageView imageView= (ImageView) anchorPaneClicked.getChildren().get(0);
                String url= imageView.getImage().getUrl();
                int result;
                if(url.equals("jetbrains://idea/navigate/reference?project=Eryantis&path=images/pawns/student_green.png")){
                    result=0;
                }
                else if(url.equals("jetbrains://idea/navigate/reference?project=Eryantis&path=images/pawns/student_red.png")){
                    result=1;
                }
                else if(url.equals("jetbrains://idea/navigate/reference?project=Eryantis&path=images/pawns/student_yellow.png")){
                    result=2;
                }
                else if(url.equals("jetbrains://idea/navigate/reference?project=Eryantis&path=images/pawns/student_pink.png")){
                    result=3;
                }
                else{
                    result=4;
                }
                ClientController.getInstance().getConsole().setPawnColour(result);
            }
        }

    }

    @FXML
    void setStudentOnGameBoard(DragEvent event) {
        if(ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.ACTION) {
            ClientController.getInstance().getConsole().setPawnWhere(-1);
        }

    }


    /**changes view of player's coins tab if in complex mode when tab button clickd*/
    @FXML
    public void showCoinsPlancia(Event event) {
        if(ClientController.getInstance().getGameStatePojo().isExpert()){
            Tab tab = (Tab )event.getTarget();
            if(tab.getId().equals("Plancia1")){
                GuiController.getInstance().setRunnable(()-> GuiController.getInstance().activeCoins(1));
                GuiController.getInstance().runMethod();
            }else if(tab.getId().equals("Plancia2")){
                GuiController.getInstance().setRunnable(()-> GuiController.getInstance().activeCoins(2));
                GuiController.getInstance().runMethod();
            }else if(tab.getId().equals("Plancia3")){
                GuiController.getInstance().setRunnable(()-> GuiController.getInstance().activeCoins(3));
                GuiController.getInstance().runMethod();
            }
        }
    }


    @FXML
    void setAssistantCardChosen1(MouseEvent event) {
        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(1);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }


    }

    @FXML
    void setAssistantCardChosen2(MouseEvent event) {
        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(2);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen3(MouseEvent event) {
        System.out.println("click 3");
        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(3);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }
    @FXML
    void setAssistantCardChosen4(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(4);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen5(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(5);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }
    @FXML
    void setAssistantCardChosen6(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(6);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen7(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(7);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }
    @FXML
    void setAssistantCardChosen8(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(8);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen9(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(9);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }

    @FXML
    void setAssistantCardChosen10(MouseEvent event) {

        if (ClientController.getInstance().getGameStatePojo().getCurrentPlayer().getNickname().equals(ClientController.getInstance().getNickname()) &&
                ClientController.getInstance().getGameStatePojo().getCurrentPhase() == Phase.PIANIFICATION){
            ClientController.getInstance().getConsole().setAssistantCardPlayed(10);
            isCloudTurn=false;
            ClientController.getSemaphore().release();
        }
    }


}

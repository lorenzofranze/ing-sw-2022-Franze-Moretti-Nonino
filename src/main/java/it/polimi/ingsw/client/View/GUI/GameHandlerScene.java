package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**class that responses to the click event on a game object, all the methods verify if the action is valid and unlock
 * the game if the choose is ok otherwise no action
 */
public class GameHandlerScene {

    @FXML
    private ImageView Cloud1;

    @FXML
    private ImageView Cloud2;

    @FXML
    private ImageView Cloud3;

    @FXML
    private Button blueOnCloud1;

    @FXML
    private Button blueOnCloud2;

    @FXML
    private Button blueOnCloud3;

    @FXML
    private Button greenOnCloud1;

    @FXML
    private Button greenOnCloud2;

    @FXML
    private Button greenOnCloud3;

    @FXML
    private Button redOnCloud1;

    @FXML
    private Button redOnCloud2;

    @FXML
    private Button redOnCloud3;

    @FXML
    private Button yellowOnCloud1;

    @FXML
    private Button yellowOnCloud2;

    @FXML
    private Button yellowOnCloud3;


    /*

    @FXML
    void setCloudChosen1(MouseEvent event) {
        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        CloudPojo cloud= ClientController.getInstance().getGameStatePojo().getClouds().get(0);
        if(cloud.getStudents().getPawns().get(ColourPawn.Green)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Red)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Yellow)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Blue)==0 ||
                isAssistantCardChosen==false){
            return;
        }
        else{
            isAssistantCardChosen=false;
            redOnCloud1.setId("0");
            yellowOnCloud1.setId("0");
            greenOnCloud1.setId("0");
            blueOnCloud1.setId("0");
            isCloudChosen = true;
            cloudChosen=0;
        }

    }

    @FXML
    void setCloudChosen2(MouseEvent event) {
        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        CloudPojo cloud= ClientController.getInstance().getGameStatePojo().getClouds().get(1);
        if(cloud.getStudents().getPawns().get(ColourPawn.Green)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Red)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Yellow)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Blue)==0 ||
                isAssistantCardChosen==false){
            return;
        }
        else{
            isAssistantCardChosen=false;
            redOnCloud1.setId("0");
            yellowOnCloud2.setId("0");
            greenOnCloud2.setId("0");
            blueOnCloud2.setId("0");
            isCloudChosen=true;
            cloudChosen=1;
        }

    }


    @FXML
    void setCloudChosen3(MouseEvent event) {
        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        CloudPojo cloud= ClientController.getInstance().getGameStatePojo().getClouds().get(2);
        if(cloud.getStudents().getPawns().get(ColourPawn.Green)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Red)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Yellow)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Blue)==0 ||
                isAssistantCardChosen==false || isCloudChosen==true){
            return;
        }
        else{
            redOnCloud1.setId("0");
            yellowOnCloud1.setId("0");
            greenOnCloud1.setId("0");
            blueOnCloud1.setId("0");
            isCloudChosen=true;
            cloudChosen=2;
        }

    }
    */


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
        System.out.println("click 3");
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
}

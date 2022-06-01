package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;

/**class that responses to the click event on a character card, all the methods verify if the action is valid and unlock
 * the game if the choose is ok otherwise no action
 */
public class GameHandlerScene {

    private HashSet<Integer> visibleAssistantCards;

    @FXML
    private ImageView AssistantCard1;
    @FXML
    private ImageView AssistantCard2;
    @FXML
    private ImageView AssistantCard3;
    @FXML
    private ImageView AssistantCard4;
    @FXML
    private ImageView AssistantCard5;
    @FXML
    private ImageView AssistantCard6;
    @FXML
    private ImageView AssistantCard7;
    @FXML
    private ImageView AssistantCard8;
    @FXML
    private ImageView AssistantCard9;
    @FXML
    private ImageView AssistantCard10;


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



    public void initialize() {
        ClientController clientController = ClientController.getInstance();
        PlayerPojo me = null;
        for (PlayerPojo p : clientController.getGameStatePojo().getPlayers()) {
            if (p.getNickname().equals(ClientController.getInstance().getNickname())) {
                me = p;
            }
        }
        if(clientController.getGameStatePojo().getPlayers().size()==2){
            Cloud3.setImage(null);
        }

        visibleAssistantCards= new HashSet<>();
        visibleAssistantCards.clear();
        Set<AssistantCardPojo> assistantCardPojos= me.getDeck();
        for(AssistantCardPojo a: assistantCardPojos) {
            if (a.getTurnOrder()== 1) {
                visibleAssistantCards.add(1);
            }
            if (a.getTurnOrder()== 2) {
                visibleAssistantCards.add(2);
            }
            if (a.getTurnOrder()== 3) {
                visibleAssistantCards.add(3);
            }
            if (a.getTurnOrder()== 4) {
                visibleAssistantCards.add(4);
            }
            if (a.getTurnOrder()== 5) {
                visibleAssistantCards.add(5);
            }
            if (a.getTurnOrder()== 6) {
                visibleAssistantCards.add(6);
            }
            if (a.getTurnOrder()== 7) {
                visibleAssistantCards.add(7);
            }
            if (a.getTurnOrder()== 8) {
                visibleAssistantCards.add(8);
            }
            if (a.getTurnOrder()== 9) {
                visibleAssistantCards.add(9);
            }
            if (a.getTurnOrder()== 10) {
                visibleAssistantCards.add(10);;
            }
        }

        if (me!=null && clientController.getGameStatePojo().getCurrentPhase().equals(Phase.PIANIFICATION)) {

            AssistantCard1.setDisable(false);
            AssistantCard2.setDisable(false);
            AssistantCard3.setDisable(false);
            AssistantCard4.setDisable(false);
            AssistantCard5.setDisable(false);
            AssistantCard6.setDisable(false);
            AssistantCard7.setDisable(false);
            AssistantCard8.setDisable(false);
            AssistantCard9.setDisable(false);
            AssistantCard10.setDisable(false);
            Cloud1.setDisable(true);
            Cloud2.setDisable(true);
            Cloud3.setDisable(true);
        }

        if (clientController.getGameStatePojo().getCurrentPhase().equals(Phase.ACTION)) {
            AssistantCard1.setDisable(true);
            AssistantCard2.setDisable(true);
            AssistantCard3.setDisable(true);
            AssistantCard4.setDisable(true);
            AssistantCard5.setDisable(true);
            AssistantCard6.setDisable(true);
            AssistantCard7.setDisable(true);
            AssistantCard8.setDisable(true);
            AssistantCard9.setDisable(true);
            AssistantCard10.setDisable(true);
            Cloud1.setDisable(true);
            Cloud2.setDisable(true);
            Cloud3.setDisable(true);
        }

        if(!visibleAssistantCards.contains(1)){
            AssistantCard1.setDisable(true);
            AssistantCard1.setImage(null);
        }
        if(!visibleAssistantCards.contains(2)){
            AssistantCard2.setDisable(true);
            AssistantCard2.setImage(null);
        }
        if(!visibleAssistantCards.contains(3)){
            AssistantCard3.setDisable(true);
            AssistantCard3.setImage(null);
        }
        if(!visibleAssistantCards.contains(4)){
            AssistantCard4.setDisable(true);
            AssistantCard4.setImage(null);
        }
        if(!visibleAssistantCards.contains(5)){
            AssistantCard5.setDisable(true);
            AssistantCard5.setImage(null);
        }
        if(!visibleAssistantCards.contains(6)){
            AssistantCard6.setDisable(true);
            AssistantCard6.setImage(null);
        }
        if(!visibleAssistantCards.contains(7)){
            AssistantCard7.setDisable(true);
            AssistantCard7.setImage(null);
        }
        if(!visibleAssistantCards.contains(8)){
            AssistantCard8.setDisable(true);
            AssistantCard8.setImage(null);
        }
        if(!visibleAssistantCards.contains(9)){
            AssistantCard9.setDisable(true);
            AssistantCard9.setImage(null);
        }
        if(!visibleAssistantCards.contains(10)){
            AssistantCard10.setDisable(true);
            AssistantCard10.setImage(null);
        }

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
}

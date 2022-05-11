package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.logic.GamePhase;

import java.io.IOException;

public class Console {

    private enum ActionBookMark{none, moveStudents, placeMotherNature}

    private Phase currentPhase = null;
    private ActionBookMark currActionBookMark = ActionBookMark.none;

    private int assistantCardPlayed = 0;
    private int studentMoved = 0;


    public void play(){
        currentPhase = ClientController.getInstance().getGameStatePojo().getCurrentPhase();

        switch (currentPhase){
            case PIANIFICATION:
                playPianification();
                break;
            case ACTION:
                playAction();
                break;
        }
    }

    private void playPianification(){
        ClientController clientController = ClientController.getInstance();
        clientController.view.chooseAssistantCard();
        NetworkHandler networkHandler = clientController.getNetworkHandler();
        GameMessage gameMessage = new GameMessage(TypeOfMove.AssistantCard, assistantCardPlayed);
        Message receivedMessage;
        boolean moveAccepted = false;

        try {
            networkHandler.sendToServer(gameMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        do{
            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    moveAccepted = true;
                }
            }else{
                ClientController.getInstance().view.showMessage(receivedMessage);
            }
        }while(moveAccepted==false);
    }

    private void playAction(){
        switch (currActionBookMark){
            case none:
                int studentsToMove = 0;
                if (ClientController.getInstance().getGameStatePojo().getPlayers().size() == 2){
                    studentsToMove = 3;
                }else {studentsToMove = 4;}
                for(int i = 0; i < studentsToMove; i++){
                    moveStudent();
                }
        }

    }

    public void setAssistantCardPlayed(int assistantCardPlayed) {
        this.assistantCardPlayed = assistantCardPlayed;
    }

    public void moveStudent(){

    }
}

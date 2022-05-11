package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.logic.GamePhase;

import java.io.IOException;

public class Console {

    ClientController clientController = ClientController.getInstance();
    GameMode gameMode = clientController.getGameMode();
    NetworkHandler networkHandler = clientController.getNetworkHandler();

    private enum ActionBookMark{none, moveStudents, placeMotherNature}

    Phase currentPhase = null;
    ActionBookMark currActionBookMark = ActionBookMark.none;

    private int assistantCardPlayed = 0;
    private int studentMoved = 0;


    public void play(){
        currentPhase = clientController.getGameStatePojo().getCurrentPhase();

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
        Message receivedMessage;
        boolean moveAccepted = false;
        do{
            clientController.viewBegin.chooseAssistantCard();
            GameMessage gameMessage = new GameMessage(TypeOfMove.AssistantCard, assistantCardPlayed);


            try {
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }


            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    moveAccepted = true;
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                clientController.viewBegin.showMessage(receivedMessage);
                ErrorMessage errorMessage=(ErrorMessage)receivedMessage;
                {
                    if(!errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice)){
                        break;
                    }
                }
            }
        }while(moveAccepted==false);
    }

    private void playAction(){
        switch (currActionBookMark){
            case none:
                int studentsToMove = 0;
                if (clientController.getGameStatePojo().getPlayers().size() == 2){
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

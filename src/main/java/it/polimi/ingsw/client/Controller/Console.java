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

    private enum ActionBookMark{}

    Phase currentPhase = null;
    ActionBookMark currActionBookMark;

    private int assistantCardPlayed = 0;


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
        clientController.viewBegin.chooseAssistantCard();
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
                clientController.viewBegin.showMessage(receivedMessage);
            }
        }while(moveAccepted==false);
    }

    private void playAction(){
        switch (currActionBookMark){

        }

    }

    public void setAssistantCardPlayed(int assistantCardPlayed) {
        this.assistantCardPlayed = assistantCardPlayed;
    }
}

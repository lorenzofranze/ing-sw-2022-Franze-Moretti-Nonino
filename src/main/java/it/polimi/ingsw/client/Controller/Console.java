package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;

import java.io.IOException;

public class Console {

    ClientController clientController = ClientController.getInstance();
    GameMode gameMode = clientController.getGameMode();
    NetworkHandler networkHandler = clientController.getNetworkHandler();

    private enum ActionBookMark{}

    Phase currentPhase;
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

        receivedMessage = networkHandler.getReceivedMessage();
        if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
            AckMessage ackMessage = (AckMessage) receivedMessage;
            if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                moveAccepted = true;
            }
        }

        if (moveAccepted == false){
            clientController.viewBegin.showMessage(receivedMessage);
        }
    }

    private void playAction(){
        switch (currActionBookMark){

        }

    }

    public void setAssistantCardPlayed(int assistantCardPlayed) {
        this.assistantCardPlayed = assistantCardPlayed;
    }
}

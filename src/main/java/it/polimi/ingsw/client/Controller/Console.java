package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.client.View.CLIView;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.logic.GamePhase;

import java.io.IOException;

public class Console {

    private enum ActionBookMark{none, moveStudents, placeMotherNature}

    private Phase currentPhase = null;
    private ActionBookMark currActionBookMark = ActionBookMark.none;

    private int assistantCardPlayed = -1;
    private Integer characterPlayed = null;
    private int studentMoved = -1;


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
        NetworkHandler networkHandler = clientController.getNetworkHandler();

        Message receivedMessage;
        GameMessage gameMessage;
        boolean moveAccepted = false;

        do{
            clientController.view.chooseAssistantCard();
            gameMessage = new GameMessage(TypeOfMove.AssistantCard, assistantCardPlayed);

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
                    clientController.view.showMessage(receivedMessage);
                    ErrorMessage errorMessage=(ErrorMessage)receivedMessage;
                    if(!errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice)){
                            break;
                    }
            }
        }while(moveAccepted==false);
    }

    private void playAction(){
        View view = ClientController.getInstance().view;
        GameStatePojo gameStatePojo = ClientController.getInstance().getGameStatePojo();

        int studentsToMove;
        if (gameStatePojo.getPlayers().size() == 2){
            studentsToMove = 3;
        }else{
            studentsToMove = 4;
        }

        switch (currActionBookMark){
            case none:
                for(int i = 0; i < studentsToMove; i++){
                    if (gameStatePojo.isExpert()) {
                        askForCharacter();
                    }
                    moveStudent();
                }
        }

    }

    public void setAssistantCardPlayed(int assistantCardPlayed) {
        this.assistantCardPlayed = assistantCardPlayed;
    }

    public void setCharacterPlayed(Integer characterPlayed) {
        this.characterPlayed = characterPlayed;
    }

    public void moveStudent(){
        System.out.println("FLAG MOVE STUDENT - CONSOLE - ACTIONPHASE");

    }

    public void askForCharacter(){
        View view = ClientController.getInstance().view;
        NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();
        Message receivedMessage;

        boolean valid = false;
        do{
            view.askForCharacter();
            GameMessage gameMessage = new GameMessage(TypeOfMove.CharacterCard, characterPlayed);

            try {
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    valid = true;
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage=(ErrorMessage)receivedMessage;
                if(!errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice)){
                    break;
                }
            }
        }while(valid == false);
    }
}

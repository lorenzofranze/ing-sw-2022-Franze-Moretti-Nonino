package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.client.View.CLIView;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.logic.GamePhase;

import java.io.IOException;

public class Console{

    private enum ActionBookMark{moveStudents, placeMotherNature, chooseCloud}

    private Phase currentPhase = null;
    private ActionBookMark currActionBookMark = ActionBookMark.moveStudents;

    private int assistantCardPlayed = -1;
    private Integer characterPlayed = null;
    private Integer pawnColour = null;
    private Integer pawnWhere = null;
    private Integer stepsMotherNature = null;
    private Integer cloudChosen = null;
    private boolean gameOver = false;

    private CharacterCardsConsole characterCardsConsole = ClientController.getInstance().getCharacterCardsConsole(); //only methods

    /**
     * checks form the last update in which Phase the game is currently in (Pianification or Action) and calls the
     * correct play method
     */
    public void play(){
        currentPhase = ClientController.getInstance().getGameStatePojo().getCurrentPhase();

        if(ClientController.getInstance().isDisconnected()) return;

        switch (currentPhase){
            case PIANIFICATION:
                playPianification();
                break;
            case ACTION:
                playAction();
                break;
        }
    }

    /**It lets the player choose his/her AssistantCard. If the card chosen is not valid, an error message is received.
     * The method terminates only if a valid card has been chosen and an ack message is received*/
    private void playPianification(){
        ClientController clientController = ClientController.getInstance();
        NetworkHandler networkHandler = clientController.getNetworkHandler();

        Message receivedMessage=null;
        GameMessage gameMessage;
        boolean moveAccepted = false;

        do{
            clientController.view.chooseAssistantCard();
            gameMessage = new GameMessage(TypeOfMove.AssistantCard, assistantCardPlayed);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!ClientController.getInstance().isDisconnected()){
                receivedMessage = networkHandler.getReceivedMessage();
            }


            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    moveAccepted = true;
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                    clientController.view.showMessage(receivedMessage);
                    ErrorMessage errorMessage=(ErrorMessage)receivedMessage;
                    if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice) ||
                            errorMessage.getTypeOfError().equals(TypeOfError.AlreadyPlayed))){
                            break;
                    }
            }
        }while(moveAccepted==false);
    }

    /**It lets the player play his/her ActionPhase. According to currActionBookmark, the player can:
     * 1. (currActionBookMark = moveStudents) -> move 3 (2-players-mode) or 4 (3-players-mode) students from his/her
     * entrance. For each student the method moveStudent is called.
     * 2. (currActionBookMark = placeMotherNature) -> The mother nature move is handled by the method placeMotherNature.
     * 3. (currActionBookMark = chooseCloud) -> the player chooses a cloud. The method chooseCloud is called.
     * Once the action is completed, the currActionBookMark is changed.*/
    private void playAction(){
        View view = ClientController.getInstance().view;
        GameStatePojo gameStatePojo = ClientController.getInstance().getGameStatePojo();

        int studentsToMoveTotal;
        if (gameStatePojo.getPlayers().size() == 2){
            studentsToMoveTotal = 3;
        }else{
            studentsToMoveTotal = 4;
        }

        switch (currActionBookMark){
            case moveStudents:
                for(int i = 0; i < studentsToMoveTotal; i++){
                    askForCharacter();
                    if (gameOver == true){
                        view.showGameState(ClientController.getInstance().getGameStatePojo());
                        return;
                    }
                    moveStudent();
                }
                askForCharacter();
                currActionBookMark = ActionBookMark.placeMotherNature;
                break;
            case placeMotherNature:
                placeMotherNature();
                if (gameOver == true){
                    view.showGameState(ClientController.getInstance().getGameStatePojo());
                    return;
                }else{
                    askForCharacter();
                }
                currActionBookMark = ActionBookMark.chooseCloud;
                break;
            case chooseCloud:
                int studentLimit  = gameStatePojo.getPlayers().size() == 2 ? 3 : 4;
                if (gameStatePojo.getStudentsBag().pawnsNumber() >= studentLimit){
                    chooseCloud();
                }
                currActionBookMark = ActionBookMark.moveStudents;
                break;
        }

    }

    /**Makes the player choose a student and where to place it through the view. A message containing this information
     * is sent to server. If an error message is received the player must remake his decision; if an ack message is
     *received, an update is requested from server.*/
    public void moveStudent(){
        ClientController clientController = ClientController.getInstance();
        NetworkHandler networkHandler = clientController.getNetworkHandler();
        View view = clientController.view;
        Message receivedMessage = null;

        boolean valid = false;
        do{
            view.moveStudent();
            GameMessage gameMessage = new PawnMovementMessage(pawnColour, pawnWhere);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!clientController.isDisconnected()){
                receivedMessage = networkHandler.getReceivedMessage();
            }

            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    valid = true;
                    receivedMessage = networkHandler.getReceivedMessage();
                    if (receivedMessage.getMessageType() == TypeOfMessage.Update){ //update student move
                        UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                        ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
                        view.showMessage(receivedMessage);
                    }else{
                        //unexpected message
                        view.showMessage(receivedMessage);
                    }
                }else{
                    //unexpected message
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice) || errorMessage.getTypeOfError().equals(TypeOfError.FullDiningRoom))){
                    break;
                }
            }
        }while(valid == false);
    }

    /**The player is asked if he/she wants to play a CharacterCad through the view.
     * YES -> a message containing the card chosen is sent to server.
     * NO -> a message containing characterPlayed = null is sent to server.
     *If an error message is received the player must remake his decision; if the player decision is valid, multiple
     *acks/updates are received.
     * 1^ ack received -> the client waits for the update of coins and characterCardsConsole.playEffect(currentCharacterID)
     * is called;
     * 2^ ack received -> the effect has been correctly used and the client waits for two more updates.*/
    public void askForCharacter(){
        NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();
        Message receivedMessage;
        View view = ClientController.getInstance().view;

        if (ClientController.getInstance().getGameStatePojo().isExpert()) {
            boolean valid = false;
            do {
                view.askForCharacter();
                GameMessage gameMessage = new GameMessage(TypeOfMove.CharacterCard, characterPlayed);

                try {
                    if (ClientController.getInstance().isDisconnected()) return;
                    networkHandler.sendToServer(gameMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                receivedMessage = networkHandler.getReceivedMessage();
                if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)) {
                    AckMessage ackMessage = (AckMessage) receivedMessage;
                    if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)) {
                        valid = true;
                        //if the player has played a characterCard (and characterPlayed != null), an update is received.
                        // Here I handle the updateMessage
                        if (characterPlayed != null) {
                            receivedMessage = networkHandler.getReceivedMessage();
                            if (receivedMessage.getMessageType() == TypeOfMessage.Update) { //update coins
                                UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                                ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
                                view.showMessage(receivedMessage);
                                //handling character card effect
                                int currentCharacterID = updateMessage.getGameState().getActiveEffect().getCharacterId();
                                characterCardsConsole.playEffect(currentCharacterID);

                                receivedMessage = networkHandler.getReceivedMessage();
                                valid = false;
                                if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)) {
                                    ackMessage = (AckMessage) receivedMessage; //end of effect
                                    if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)) {
                                        valid = true;
                                    }
                                }
                                if (valid == false) {
                                    //unexpected message
                                    view.showMessage(receivedMessage);
                                }
                            } else {
                                //unexpected message
                                view.showMessage(receivedMessage);
                            }
                        }
                    } else {
                        //unexpected message
                        view.showMessage(receivedMessage);
                    }
                } else if (receivedMessage.getMessageType().equals(TypeOfMessage.Error)) {
                    view.showMessage(receivedMessage);
                    ErrorMessage errorMessage = (ErrorMessage) receivedMessage;
                    if (!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice) || (errorMessage.getTypeOfError().equals(TypeOfError.NoMoney)))) {
                        break;
                    }
                }
                characterPlayed = null;
            } while (valid == false);

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType() == TypeOfMessage.Update) { //update end ask for character
                UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
                view.showMessage(receivedMessage);

                receivedMessage = networkHandler.getReceivedMessage();
                if (receivedMessage.getMessageType() == TypeOfMessage.Update) { //update game ended
                    updateMessage = (UpdateMessage) receivedMessage;
                    ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
                    view.showMessage(receivedMessage);
                    if (ClientController.getInstance().getGameStatePojo().isGameOver()){
                        gameOver = true;
                    }
                }

            } else {
                //unexpected message
                view.showMessage(receivedMessage);
            }
        }
    }

    /**Makes the player choose where to place mother nature. A message containing this information is sent to server.
     * If an error message is received the player must remake his decision; if an ack message is received, 2 updates
     * (update mother nature place and update possible endgame) are requested from server.*/
    public void placeMotherNature(){
        View view = ClientController.getInstance().view;
        NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();
        Message receivedMessage;

        boolean valid = false;
        do{
            view.placeMotherNature();
            GameMessage gameMessage = new GameMessage(TypeOfMove.MoveMotherNature, stepsMotherNature);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(ClientController.getInstance().isDisconnected()) return;
            receivedMessage = networkHandler.getReceivedMessage();

            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    valid = true;
                }else{
                    //unexpected message
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))) {
                    break;
                }
            }
            stepsMotherNature = null;
        }while(valid == false);

        receivedMessage = networkHandler.getReceivedMessage();
        if (receivedMessage.getMessageType() == TypeOfMessage.Update){ //update motherNature place
            UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
            ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
            view.showMessage(receivedMessage);
        }else{
            //unexpected message
            view.showMessage(receivedMessage);
        }

        receivedMessage = networkHandler.getReceivedMessage();
        if (receivedMessage.getMessageType() == TypeOfMessage.Update) { //update game ended
            UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
            ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
            view.showMessage(receivedMessage);
            if (ClientController.getInstance().getGameStatePojo().isGameOver()){
                gameOver = true;
            }
        }
    }

    /**Makes the player choose a cloud. A message containing this information is sent to server.
     * If an error message is received the player must remake his decision; if an ack message is received, an update is
     * requested from server.*/
    public void chooseCloud(){
        View view = ClientController.getInstance().view;
        NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();
        Message receivedMessage;

        boolean valid = false;
        do{
            view.chooseCloud();
            GameMessage gameMessage = new GameMessage(TypeOfMove.CloudChoice, cloudChosen);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();

            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    valid = true;
                }else{
                    //unexpected message
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))) {
                    break;
                }
            }
            cloudChosen = null;
        }while(valid == false);


        receivedMessage = networkHandler.getReceivedMessage();
        if (receivedMessage.getMessageType() == TypeOfMessage.Update){
            UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
            ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
            view.showMessage(receivedMessage);
        }else{
            //unexpected message
            view.showMessage(receivedMessage);
        }
    }

    public void setPawnColour(Integer pawnColour) {
        this.pawnColour = pawnColour;
    }

    public void setPawnWhere(Integer pawnWhere) {
        this.pawnWhere = pawnWhere;
    }

    public void setStepsMotherNature(Integer stepsMotherNature) {
        this.stepsMotherNature = stepsMotherNature;
    }

    public void setCloudChosen(Integer cloudChosen) {
        this.cloudChosen = cloudChosen;
    }

    public void setAssistantCardPlayed(int assistantCardPlayed) {
        this.assistantCardPlayed = assistantCardPlayed;
    }

    public void setCharacterPlayed(Integer characterPlayed) {
        this.characterPlayed = characterPlayed;
    }
}

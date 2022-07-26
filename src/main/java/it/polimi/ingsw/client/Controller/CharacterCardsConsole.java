package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.common.messages.*;

import java.io.IOException;

/**
 * Handles the game when a character card is active:
 * calls the correct method according to the character card id.
 * The methods, like the ones in the Console-class, call the correct method in the view. The method
 * in the view set values to the attribute of the Console.
 * */
public class CharacterCardsConsole {
    private View view = ClientController.getInstance().view;
    private NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();

    private int pawnColour;
    private int pawnWhere;
    private int pawnsToMove;


    /**
     * Chooses the effect to play according to the character card id
     * @param id
     */
    public void playEffect(int id){
        if(id==1){
            this.playEffectCard_1();
        }else if(id==3 || id==5){
            this.playEffectCard_3_5();
        }else if(id==7 || id==10) {
            this.playEffectCard_7_10();
        }else if(id==9 || id==11 || id==12) {
            this.playEffectCard_9_11_12();
        }else if(id==2 || id==4 || id ==6 || id==8) {
            view.showEffect(id);
            return; // no action needed by the player
        }
    }

    /**
     * The player chooses a pawn among those on the card and the island wherw he wants to place the pawn
     */
    private void playEffectCard_1(){
        Message receivedMessage = null;
        boolean valid = false;
        do{
            view.moveStudentToIsland();
            GameMessage gameMessage = new PawnMovementMessage(pawnColour, pawnWhere);

            try {
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)) {
                    valid = true;
                }else{
                    //messaggio imprevisto
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))){
                    break;
                }
            }
        }while(valid == false);

    }

    /**
     * the player chooses an island:
     * ---> card3: mother nature won't calculate the influence once it stops on that island
     * ---> card5: puts a noentrytile on that island
     */
    public void playEffectCard_3_5(){
        Message receivedMessage = null;
        boolean valid = false;
        do{
            view.chooseIsland();
            GameMessage gameMessage = new GameMessage(TypeOfMove.IslandChoice , pawnWhere);

            try {
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)) {
                    valid = true;
                }else{
                    //messaggio imprevisto
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))){
                    break;
                }
            }
        }while(valid == false);
    }

    /**
     * ---> card7: swap up to 3 pawns on the card with the player's entry
     * ---> card10: swap up to 2 pawns on the player's entry and board
     */
    private void playEffectCard_7_10() {
        Message receivedMessage = null;
        boolean valid = false;
        do{
            view.chooseNumOfMove();
            GameMessage gameMessage = new GameMessage(TypeOfMove.NumOfMove, pawnsToMove);

            try {
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                view.showMessage(receivedMessage);
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)) {
                    valid = true;
                }else{
                    //messaggio imprevisto
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))){
                    return;
                }
            }
        }while(valid == false);

        for(int i=0; i<pawnsToMove; i++){
            //first student colour
            do{
                valid = false;
                view.chooseColour();
                GameMessage gameMessage = new GameMessage(TypeOfMove.StudentColour , pawnColour);

                try {
                    networkHandler.sendToServer(gameMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                receivedMessage = networkHandler.getReceivedMessage();
                if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                    AckMessage ackMessage = (AckMessage) receivedMessage;
                    if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)) {
                        valid = true;
                    }else{
                        //messaggio imprevisto
                        view.showMessage(receivedMessage);
                    }
                }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                    view.showMessage(receivedMessage);
                    ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                    if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))){
                        break;
                    }
                }
            }while(valid == false);
            //second student colour
            do{
                valid = false;
                view.chooseColour();
                GameMessage gameMessage = new GameMessage(TypeOfMove.StudentColour , pawnColour);

                try {
                    networkHandler.sendToServer(gameMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                receivedMessage = networkHandler.getReceivedMessage();
                if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                    AckMessage ackMessage = (AckMessage) receivedMessage;
                    if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)) {
                        valid = true;
                    }else{
                        //messaggio imprevisto
                        view.showMessage(receivedMessage);
                    }
                }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                    view.showMessage(receivedMessage);
                    ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                    if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))){
                        break;
                    }
                }
            }while(valid == false);

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType() == TypeOfMessage.Update){
                UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
                view.showMessage(receivedMessage);
            }else{
                //messaggio imprevisto
                view.showMessage(receivedMessage);
            }

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                //ok
            }else{
                //messaggio imprevisto
                view.showMessage(receivedMessage);
            }
        }
    }

    /**
     * the player chooses a pawn among those on the card
     * ---> card9: the colour do not influence the influence-scores in this turn
     * ---> card11: the pawn goes on the player board
     * ---> card12: all players put three pawns of that color on the current player's board
     */
    private void playEffectCard_9_11_12() {
        Message receivedMessage = null;
        boolean valid = false;
        do{
            view.chooseColour();
            GameMessage gameMessage = new GameMessage(TypeOfMove.StudentColour , pawnColour);

            try {
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)) {
                    valid = true;
                }else{
                    //messaggio imprevisto
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))){
                    break;
                }
            }
        }while(valid == false);
    }

    //getter and setter

    public void setPawnColour(int pawnColour) {
        this.pawnColour = pawnColour;
    }

    public void setPawnWhere(int pawnWhere) {
        this.pawnWhere = pawnWhere;
    }

    public void setPawnsToMove(int pawnsToMove) {
        this.pawnsToMove = pawnsToMove;
    }
}

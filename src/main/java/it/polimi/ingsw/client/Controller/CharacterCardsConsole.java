package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.common.messages.*;

import java.io.IOException;

public class CharacterCardsConsole {
    private View view = ClientController.getInstance().view;
    private NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();

    private int pawnColour;
    private int pawnWhere;
    private int pawnsToMove;


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
            return; // no action needed by the player
        }
    }

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
        }
        receivedMessage = networkHandler.getReceivedMessage();
        if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
            //ok
        }else{
            //messaggio imprevisto
            view.showMessage(receivedMessage);
        }
    }

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

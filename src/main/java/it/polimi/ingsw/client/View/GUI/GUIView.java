package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.CharacterCardsConsole;
import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.Console;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;

import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Game;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;


public class GUIView implements View {

    private Consumer<Boolean> nameCompleteObserver;

    private Consumer<Boolean> noEnoughCoinsObserver;

    private Consumer<Boolean> invalidChoiseObserver;

    /**
     * when the client controller calls chooseGameMode, the client controller's attribute "gameMode" is
     * set with the value chosen in ChooseGameModeScene.
     * The semaphore is initialized with 0, so that the thread waits in chooseGameMode until someone calls
     * semaphore.release. This is done by a JavaFX thread at the end of inputChoice1/2/3/4()
     * (in  the class ChooseGameModeScene)
     */
    @Override
    public synchronized void chooseGameMode() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * when the client controller calls beginReadUsername, the client controller's attribute "nickname" is
     * set with the value chosen in ChooseNameScene.
     * The semaphore is 0, so that the thread waits in beginReadUsername until someone calls
     * semaphore.release. This is done by a JavaFX thread at the end of nameCheck()
     * (in  the class ChooseNameScene)
     */
    @Override
    public void beginReadUsername() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void chooseAssistantCard() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void moveStudent() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeMotherNature() {
        //GuiController.getInstance().setRunnable(()->GuiController.getInstance().changeCursor(1));
        //GuiController.getInstance().runMethod();
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //getInstance().setRunnable(()->GuiController.getInstance().changeCursor(0));
        //GuiController.getInstance().runMethod();
    }





    @Override
    public synchronized void chooseCloud() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Override

    public synchronized void showMessage(Message message) {
        if(message==null) return;

        if(message.getMessageType().equals(TypeOfMessage.Ack)){
            AckMessage ackMessage = (AckMessage) message;
            showAck(ackMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Update)){
            UpdateMessage updateMessage = (UpdateMessage) message;
            showUpdate(updateMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Game)){
            GameMessage gameMessage = (GameMessage) message;
            showMove(gameMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Error)){
            ErrorMessage errorMessage = (ErrorMessage) message;
            showError(errorMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Ping)){
            PingMessage pingMessage = (PingMessage) message;
            showPing(pingMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Async)){
            AsyncMessage asyncMessage = (AsyncMessage) message;

            showAsync(asyncMessage);
        }
    }

    @Override
    public synchronized void showError(ErrorMessage errorMessage) {
        switch(errorMessage.getTypeOfError()) {
            case UsedName:
                //notify name already in use
                if(nameCompleteObserver!=null){
                    // accept calls the consumer in nameCompleteObserver with argument false
                    //so a message of allert will be shown
                    nameCompleteObserver.accept(false);
                    nameCompleteObserver=null;
                }
                break;
            case UnmatchedMessages:
                System.out.println("Unexpected message received from server.\n");
                if(ClientController.getInstance().isDisconnected()==false){
                    ClientController.getInstance().setDisconnected();
                }
                break;
            case FullDiningRoom:
                System.out.println("The diningroom is full. Place the student in another place.\n");
                break;
            case InvalidChoice:
                if(invalidChoiseObserver!=null){
                    invalidChoiseObserver.accept(false);
                    invalidChoiseObserver=null;
                }
                break;
            case AlreadyPlayed:
                System.out.println("Another player has already played this card.\n");
                break;
            case TurnError:
                System.out.println("You cannot play. Wait for your turn.\n");
                break;
            case NoMoney:
                if(noEnoughCoinsObserver!=null){
                    noEnoughCoinsObserver.accept(false);
                    noEnoughCoinsObserver=null;
                }
                break;
            case FailedConnection:
                System.out.println("Failed connection to the server.\n");
                break;
            default:
                System.out.println("Unknown error message.\n");
        }
    }

    @Override
    public synchronized void showConnection(ConnectionMessage connectionMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringConnection = jsonConverter.fromMessageToJson(connectionMessage);
        System.out.println(connectionMessage);
    }

    @Override
    public synchronized void showAck(AckMessage ackMessage) {

        switch(ackMessage.getTypeOfAck()) {
            case CorrectConnection:
                //name ok -> change view
                GuiController.getInstance().setRunnable(()->GuiController.getInstance().switchWaitScene());
                GuiController.getInstance().runMethod();
                break;
            case CompleteLobby:
                GuiController.getInstance().setRunnable(()->GuiController.getInstance().switchGameScene());
                GuiController.getInstance().runMethod();
                break;
            default:
                System.out.println("Unknown ack message");
                break;
        }
    }

    @Override
    public synchronized void showUpdate(UpdateMessage updateMessage) {
        GameStatePojo gameStatePojo = updateMessage.getGameState();
        showGameState(gameStatePojo);
    }

    @Override
    public synchronized void showAsync(AsyncMessage asyncMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringAsync = jsonConverter.fromMessageToJson(asyncMessage);
        System.out.println(stringAsync);
    }

    @Override
    public synchronized void showMove(GameMessage gameMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringMove = jsonConverter.fromMessageToJson(gameMessage);
        System.out.println(stringMove);
    }

    @Override
    public synchronized void showPing(PingMessage pingMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringPing = jsonConverter.fromMessageToJson(pingMessage);
        System.out.println(stringPing);
    }

    @Override
    public synchronized void showGameState(GameStatePojo gameStatePojo) {
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().showGameUpdate());
        GuiController.getInstance().runMethod();
    }
    synchronized public void setNameCompleteObserver(Consumer<Boolean> nameCompleteObserver) {
        this.nameCompleteObserver = nameCompleteObserver;
    }

    public void setNoEnoughCoinsObserver(Consumer<Boolean> noEnoughCoinsObserver) {
        this.noEnoughCoinsObserver = noEnoughCoinsObserver;
    }

    public void setInvalidChoiseObserver(Consumer<Boolean> invalidChoiseObserver) {
        this.invalidChoiseObserver = invalidChoiseObserver;
    }


    //METHODS FOR COMPLEX MODE:

    @Override
    public void askForCharacter() {
        if(ClientController.getInstance().getGameStatePojo().isExpert()==true) {
            try {
                ClientController.getSemaphore().acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method used by card 1
     */
    @Override
    public synchronized void moveStudentToIsland(){
        GameHandlerScene.setCharacterCardToUse(true); // block all simple mode methods
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard1());
        GuiController.getInstance().runMethod();
        System.out.println("arrivato in 1");
        GameHandlerScene.setMoveStudentCard(true);

        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GameHandlerScene.setCharacterCardToUse(false);
        GameHandlerScene.setChooseIsland(false);
        GameHandlerScene.setPawnColour(false);
    }

    /**
     * method used by card 3 and card 5
     */
    @Override
    public synchronized void chooseIsland(){
        GameHandlerScene.setCharacterCardToUse(true);

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==3){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard3());
            GuiController.getInstance().runMethod();
            System.out.println("arrivato in 3");
            GameHandlerScene.setChooseIsland(true); // enables click on island for card's effect
        }

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==5){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard5());
            GuiController.getInstance().runMethod();
            System.out.println("arrivato in 5");
            GameHandlerScene.setChooseIsland(true);
        }

        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //back to initial conditions
        GameHandlerScene.setCharacterCardToUse(false);
        GameHandlerScene.setChooseIsland(false);
        GameHandlerScene.setPawnColour(false);

    }

    /** this method is used by card 7 and 10 to make the player choose the number of pawns he wants to move */
    @Override
    public synchronized void chooseNumOfMove() {
        GameHandlerScene.setCharacterCardToUse(true); // block all simple mode methods
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is used by card 9, 11 and 12,
     * The player chooses the pawn on the card
     */
    @Override
    public synchronized void chooseColour() {
        GameHandlerScene.setCharacterCardToUse(true); // block all simple mode methods
        //guardare nella interfaccia View da chi è chiamato questo metodo e fare controllo opportuni
        //qui sbloccare condizioni su GameHandlerScene se serve
        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==9){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard9());
            GuiController.getInstance().runMethod();
            System.out.println("arrivato in 9");
            GameHandlerScene.setPawnColour(true);
        }else if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==11){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard11());
            GuiController.getInstance().runMethod();
            System.out.println("arrivato in 11");
            GameHandlerScene.setPawnColour(true);
        }
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //bloccare tutte le condizioni e tornare alla condizione di partenza perchè effetto terminato
        GameHandlerScene.setCharacterCardToUse(false);
        GameHandlerScene.setChooseIsland(false);
        GameHandlerScene.setPawnColour(false);
    }






}
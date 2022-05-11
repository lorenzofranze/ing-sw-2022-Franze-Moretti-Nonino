package it.polimi.ingsw.client.Controller;


import it.polimi.ingsw.client.View.*;
import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;

import java.io.IOException;

import static it.polimi.ingsw.common.messages.TypeOfMessage.*;

public class ClientController implements Runnable {

    private static ClientController instance;
    private String nickname;
    private GameMode gameMode;
    private NetworkHandler networkHandler;
    private Console console;
    private GameStatePojo gameStatePojo;
    private boolean disconnected=false;

    public View view;

    private ClientController(){
        this.instance=null;
    }

    public static ClientController getInstance(){
        if (instance == null){
            instance = new ClientController();
        }
        return instance;
    }

    @Override
    public void run() {

        connect();
        if(disconnected==true){
            return;
        }
        console = new Console();

        waitForOtherPlayers();
        waitForFirstGameState();


        while (gameStatePojo.isGameOver() == false) {

            Message receivedMessage = networkHandler.getReceivedMessage();
            if(disconnected) return;

            switch (receivedMessage.getMessageType()) {
                case Connection:
                    view.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Update:
                    view.showMessage(receivedMessage);
                    UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                    this.gameStatePojo = updateMessage.getGameState();
                    if (gameStatePojo.getCurrentPlayer().getNickname().equals(nickname)) {
                        console.play();
                    }
                    break;
                case Ack:
                    view.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Game:
                    view.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Error:
                    view.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Ping:
                    view.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Async:
                    view.showMessage(receivedMessage); //DA CANCELLARE
                    return;
            }
        }
    }

    private void connect(){

        view.chooseGameMode();

        boolean valid = true;
        do {
            valid = true;
            view.beginReadUsername();
            ConnectionMessage cm = new ConnectionMessage(nickname, gameMode);

            try {
                networkHandler.sendToServer(cm);
            } catch (IOException ex) {
                System.out.println("ERROR-Login-connect-1");
                return;
            }

            Message receivedMessage = networkHandler.getReceivedMessage();
            if(disconnected) return;
            view.showMessage(receivedMessage);

            if (receivedMessage.getMessageType().equals(Ack)) {
                AckMessage responseAck = (AckMessage) receivedMessage;
                if (responseAck.getTypeOfAck().equals(TypeOfAck.CorrectConnection)) {
                    valid = true;
                    return;
                }else{
                    System.out.println("ERROR-Login-connect-3 (unexpected ack message)");
                }
            }


            if (receivedMessage.getMessageType().equals(Error)) {
                ErrorMessage errorMessage = (ErrorMessage) receivedMessage;
                valid = false;
            }
        }while (!valid);
    }


    private void waitForOtherPlayers(){
        Message receivedMessage = networkHandler.getReceivedMessage();
        if(disconnected) return;
        view.showMessage(receivedMessage); //DA CANCELLARE
        boolean allJoined = false;

        while(allJoined == false){
            if (receivedMessage.getMessageType().equals(Ack)){
                AckMessage receivedAck = (AckMessage) receivedMessage;
                if (receivedAck.getTypeOfAck().equals(TypeOfAck.CompleteLobby)){
                    allJoined = true;
                }
            } else {
                System.out.println("MESSAGGIO SCORRETTO");  //DA CANCELLARE
                receivedMessage = networkHandler.getReceivedMessage();
                if(disconnected) return;
                view.showMessage(receivedMessage); //DA CANCELLARE
            }
        }
    }

    private void waitForFirstGameState(){
        Message receivedMessage = networkHandler.getReceivedMessage();
        if(disconnected) return;
        boolean gameStateReceived = false;

        while(gameStateReceived == false){
            if (receivedMessage.getMessageType().equals(Update)){
                view.showMessage(receivedMessage);
                UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                this.gameStatePojo = updateMessage.getGameState();
                gameStateReceived = true;
                if(gameStatePojo.getCurrentPlayer().getNickname().equals(nickname)){
                    console.play();
                }
            }
            else{
                System.out.println("MESSAGGIO SCORRETTO");  //DA CANCELLARE
                view.showMessage(receivedMessage);     //DA CANCELLARE
                receivedMessage = networkHandler.getReceivedMessage();
                if(disconnected) return;
            }
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void setView(View view) {
        this.view = view;
    }

    public GameStatePojo getGameStatePojo() {
        return gameStatePojo;
    }

    public String getNickname() {
        return nickname;
    }

    public Console getConsole() {
        return console;
    }

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public void setDisconnected(){
        disconnected=true;
    }

    public boolean isDisconnected() {
        return disconnected;
    }
}
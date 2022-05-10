package it.polimi.ingsw.client.Controller;


import it.polimi.ingsw.client.View.ViewBegin;
import it.polimi.ingsw.client.View.ViewEnd;
import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.gamePojo.Phase;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;

import java.io.IOException;
import java.util.Scanner;

import static it.polimi.ingsw.common.messages.TypeOfMessage.*;

public class ClientController implements Runnable {

    private String nickname;
    private GameMode gameMode;
    private static ClientController instance;
    private NetworkHandler networkHandler;
    private Console console;

    ViewBegin viewBegin;
    ViewEnd viewEnd;

    private GameStatePojo gameStatePojo;
    Message receivedMessage;



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
        console = new Console();

        waitForOtherPlayers();
        waitForFirstGameState();

        System.out.println("FLAG -CLIENT CONTROLLER");

        while (gameStatePojo.isGameOver() == false) {

            receivedMessage = networkHandler.getReceivedMessage();

            switch (receivedMessage.getMessageType()) {
                case Connection:
                    viewBegin.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Update:
                    this.gameStatePojo = ((UpdateMessage) receivedMessage).getGameState();
                    if (gameStatePojo.getCurrentPlayer().getNickname().equals(nickname)) {
                        console.play();
                    }
                    break;
                case Ack:
                    viewBegin.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Game:
                    viewBegin.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Error:
                    viewBegin.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Ping:
                    viewBegin.showMessage(receivedMessage); //DA CANCELLARE
                    break;
                case Async:
                    viewBegin.showMessage(receivedMessage); //DA CANCELLARE
                    break;
            }
        }
    }

    public void connect(){

        viewBegin.chooseGameMode();

        boolean valid = true;
        do {
            valid = true;
            viewBegin.beginReadUsername();
            ConnectionMessage cm = new ConnectionMessage(nickname, gameMode);

            try {
                networkHandler.sendToServer(cm);
            } catch (IOException ex) {
                System.out.println("ERROR-Login-connect-1");
                return;
            }

            receivedMessage = networkHandler.getReceivedMessage();
            viewBegin.showMessage(receivedMessage);

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

    public void waitForOtherPlayers(){
        receivedMessage = networkHandler.getReceivedMessage();
        viewBegin.showMessage(receivedMessage); //DA CANCELLARE
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
                viewBegin.showMessage(receivedMessage); //DA CANCELLARE
            }
        }
    }

    public void waitForFirstGameState(){
        receivedMessage = networkHandler.getReceivedMessage();
        boolean gameStateReceived = false;

        while(gameStateReceived == false){
            if (receivedMessage.getMessageType().equals(Update)){
                viewBegin.showMessage(receivedMessage);
                UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                this.gameStatePojo = updateMessage.getGameState();
                gameStateReceived = true;
                if(gameStatePojo.getCurrentPlayer().getNickname().equals(nickname)){
                    console.play();
                }
            } else {
                System.out.println("MESSAGGIO SCORRETTO");  //DA CANCELLARE
                viewBegin.showMessage(receivedMessage);     //DA CANCELLARE
                receivedMessage = networkHandler.getReceivedMessage();
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

    public void setViewBegin(ViewBegin viewBegin) {
        this.viewBegin = viewBegin;
    }
    public void setViewEnd(ViewEnd viewEnd) {
        this.viewEnd = viewEnd;
        this.viewBegin.setViewEnd(viewEnd);
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
}
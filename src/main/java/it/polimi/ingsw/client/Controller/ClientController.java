package it.polimi.ingsw.client.Controller;


import it.polimi.ingsw.client.View.*;
import it.polimi.ingsw.client.View.GUI.GUIView;
import it.polimi.ingsw.client.View.GUI.GuiController;
import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import static it.polimi.ingsw.common.messages.TypeOfMessage.*;

public class ClientController implements Runnable {

    private static ClientController instance;
    private String nickname;
    private GameMode gameMode;
    private NetworkHandler networkHandler;
    private Thread networkHandlerThread;
    private Console console;
    private CharacterCardsConsole characterCardsConsole;
    private GameStatePojo gameStatePojo;
    private boolean disconnected=false;
    private static Semaphore semaphore = new Semaphore(0);

    private Object lock;
    public View view;

    private ClientController(){
        this.instance=null;
        this.lock=new Object();
    }

    public static ClientController getInstance(){
        if (instance == null){
            instance = new ClientController();
        }
        return instance;
    }

    public static Semaphore getSemaphore() {
        return semaphore;
    }

    /** il thread è invocato da ClientApp.
     * si occupa della connessione del client col metodo connect(),
     * una volta arrivato il primo update dal server, finchè il gioco non termina o sono arrivati errori
     * di disconnessione, se il messaggio arrivato è di tipo update(), invoca console.play()
     */
    @Override
    public void run() {
        //if view is GUI type GuiController is launched otherwise no action
        if(view instanceof GUIView){
            GuiController.main(null);
        }
        System.out.println("GUI lanciata ora 2 thread");
        Message receivedMessage=null;


        connect();

        if(disconnected==true){
            return;
        }
        characterCardsConsole = new CharacterCardsConsole();
        console = new Console();


        waitForOtherPlayers();
        if(disconnected==true){
            return;
        }
        waitForFirstGameState();



        while (gameStatePojo.isGameOver() == false && disconnected==false) {


            if(!disconnected){
                receivedMessage = networkHandler.getReceivedMessage();
            }
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
                        if(gameStatePojo.isGameOver()==false && gameStatePojo.getWinner()==null)
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

        //GAME ENDED
    }

    /**
     * si occupa della gestione dei messaggi di connessione, di errore e di ack durante la fase di connessione.
     * Il giocatore sceglie un gameMode (in locale si verifica che la modalità sia tra quelle proposte)
     * e sceglie un nickname. Viene inviato il messaggio che contiene le due informazioni e se il nome non è
     * valido si chiede di scegliere un altro nickname
     */
    private void connect(){

        this.networkHandlerThread= new Thread(networkHandler);
        this.networkHandlerThread.start();
        view.chooseGameMode();
        System.out.println("game mode chosen: " + this.gameMode); // debug

        boolean valid = true;
        do {
            valid = true;
            view.beginReadUsername();
            System.out.println("game mode chosen: " + this.nickname); // debug
            ConnectionMessage cm = new ConnectionMessage(nickname, gameMode);

            try {
                networkHandler.sendToServer(cm);
            } catch (IOException ex) {
                System.out.println("ERROR-Login-connect-1");
                disconnected=true;
                return;
            }

            Message receivedMessage=null;
            if(!disconnected){
                receivedMessage = networkHandler.getReceivedMessage();
            }
            if(disconnected) return;
            view.showMessage(receivedMessage);

            if (receivedMessage.getMessageType().equals(Ack)) {
                AckMessage responseAck = (AckMessage) receivedMessage;
                if (responseAck.getTypeOfAck().equals(TypeOfAck.CorrectConnection)) {
                    valid = true;
                    return;
                }
                else {
                        System.out.println("ERROR-Login-connect-3 (unexpected ack message)");
                        disconnected=true;
                }
            }
            else if (receivedMessage.getMessageType().equals(Error)) {
                ErrorMessage errorMessage = (ErrorMessage) receivedMessage;
                valid = false;
            }
            else if(receivedMessage.getMessageType()==Async){
                disconnected=true;
                return;
            }
        }while (!valid);

    }


    /**
     * attende l'arrivo del messaggio di ack che avvisa del riempimento della lobby e nel frattempo
     * controlla non ci siano problemi di disconnessione per i quali bisogna settare disconnected a true
     */
    private void waitForOtherPlayers(){
        Message receivedMessage=null;
        if(!disconnected){
            receivedMessage = networkHandler.getReceivedMessage();
        }

        if(disconnected) return;
        view.showMessage(receivedMessage); //DA CANCELLARE
        boolean allJoined = false;

        while(allJoined == false){
            if (receivedMessage.getMessageType().equals(Ack)){
                AckMessage receivedAck = (AckMessage) receivedMessage;
                if (receivedAck.getTypeOfAck().equals(TypeOfAck.CompleteLobby)){
                    allJoined = true;
                }
            }
            else if(receivedMessage.getMessageType().equals(Async)){
                disconnected=true;
                return;
            }
            else {
                System.out.println("MESSAGGIO SCORRETTO");  //DA CANCELLARE
                if(!disconnected){
                    receivedMessage = networkHandler.getReceivedMessage();
                }

                if(disconnected) return;
                view.showMessage(receivedMessage); //DA CANCELLARE
            }
        }

    }

    /**
     * attende per il primo messaggio di update e nel frattempo
     * controlla non ci siano problemi di disconnessione per i quali bisogna settare disconnected a true
     */
    private void waitForFirstGameState(){
        Message receivedMessage=null;
        if(!disconnected){
            receivedMessage = networkHandler.getReceivedMessage();
        }
        if(disconnected) return;
        boolean gameStateReceived = false;

        while(gameStateReceived == false){
            if (receivedMessage.getMessageType().equals(Update)){
                view.showMessage(receivedMessage);
                UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                this.gameStatePojo = updateMessage.getGameState();
                gameStateReceived = true;
            }
            else if(receivedMessage.getMessageType().equals(Async)){
                    disconnected = true;
                    return;
                }
            else{
                    //messaggio scorretto
                    view.showMessage(receivedMessage);     //DA CANCELLARE

                    if(!disconnected){
                        receivedMessage = networkHandler.getReceivedMessage();
                    }
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

    //used only for GUI mode
    public GUIView getView(){
        return (GUIView) this.view;
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

    public void setDisconnected() {
        networkHandler.getPingSenderFromClientThread().interrupt();
        networkHandlerThread.interrupt();

        disconnected = true;
        synchronized (lock) {
            try {
                lock.notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setGameStatePojo(GameStatePojo gameStatePojo) {
        this.gameStatePojo = gameStatePojo;
    }

    public CharacterCardsConsole getCharacterCardsConsole() {
        return characterCardsConsole;
    }

    public Object getLock() {
        return lock;
    }
}
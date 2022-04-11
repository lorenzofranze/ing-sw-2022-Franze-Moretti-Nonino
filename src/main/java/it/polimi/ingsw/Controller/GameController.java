package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.io.IOException;
import java.util.List;

public class GameController implements Runnable {
    private GamePhase setUpPhase;
    private GamePhase pianificationPhase;
    private GamePhase actionPhase;

    private Game game;
    private GamePhase currentPhase;
    private boolean gameOver=false;
    private boolean expert;
    private MessageHandler messageHandler;
    private Player currentPlayer;
    private Player winner;
    private Lobby lobby;



    public GameController(Lobby lobby, Integer gameid, boolean expert){
        this.game=new Game(lobby.getUsersNicknames(), gameid);
        this.expert=expert;

        this.setUpPhase=new SetUpPhase(this);
        this.messageHandler= new MessageHandler(lobby);

        this.pianificationPhase=new PianificationPhase(this);
        this.actionPhase=new ActionPhase(this);

        this.lobby=lobby;

    }

    public void run(){
        this.currentPhase=setUpPhase;
        while (!gameOver){
            currentPhase.handle();
        }
        calculateWinner();

        //closes all player's socket and the server soket
        for (String s: lobby.getUsersNicknames()){
            try {
                lobby.getUsersReadyToPlay().get(s).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            messageHandler.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Game getGame() {
        return game;
    }

    public void calculateWinner(){

        ////////////////////RICORDA: PARITà

    }

    public void setGamePhase(GamePhase state){
        this.currentPhase=state;
    }

    public GamePhase getPianificationPhase() {
        return pianificationPhase;
    }

    public GamePhase getActionPhase() {
        return actionPhase;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

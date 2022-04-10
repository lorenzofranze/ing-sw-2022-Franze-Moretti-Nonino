package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class GameController {
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



    public GameController(Lobby lobby, Integer gameid, boolean expert){
        this.game=new Game(lobby.getUsersNicknames(), gameid);
        this.expert=expert;

        this.setUpPhase=new SetUpPhase(this);
        this.messageHandler= new MessageHandler(lobby);

        this.pianificationPhase=new PianificationPhase(this);
        this.actionPhase=new ActionPhase(this);

    }

    public void play(){
        this.currentPhase=setUpPhase;
        while (!gameOver){
            currentPhase.handle();
        }
        calculateWinner();
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

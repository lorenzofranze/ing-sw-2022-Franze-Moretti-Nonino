package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class GameController {
    GamePhase setUpPhase;
    GamePhase pianificationPhase;
    GamePhase actionPhase;
    GamePhase endPhase;
    private Game game;
    private GamePhase currentPhase;
    private Player firstPianificationPlayer;

    public GameController(Lobby lobby, Integer gameid){
        this.game=new Game(lobby.getUsersReadyToPlay(), gameid);
        this.currentPhase=setUpPhase;
    }
    public Player getFirstPianificationPlayer() {
        return firstPianificationPlayer;
    }

    public void setFirstPianificationPlayer(Player firstPianificationPlayer) {
        this.firstPianificationPlayer = firstPianificationPlayer;
    }

    public Game getGame() {
        return game;
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

    public GamePhase getEndPhase() {
        return endPhase;
    }
}

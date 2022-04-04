package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class GameController {
    private Game game;
    private GamePhase currentPhase;
    private Player firstPianificationPlayer;

    public GameController(Lobby lobby, Integer gameid){

        this.game=new Game(lobby.getUsersReadyToPlay(), gameid);

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
}

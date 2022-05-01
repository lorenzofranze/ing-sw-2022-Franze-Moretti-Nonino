package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.server.model.Player;

public class SetUpResult extends PhaseResult{
    private Player firstRandomPianificationPlayer;

    public SetUpResult(){
        this.firstRandomPianificationPlayer = null;
    }

    public Player getFirstRandomPianificationPlayer() {
        return firstRandomPianificationPlayer;
    }

    public void setFirstRandomPianificationPlayer(Player firstRandomPianificationPlayer) {
        this.firstRandomPianificationPlayer = firstRandomPianificationPlayer;
    }
}

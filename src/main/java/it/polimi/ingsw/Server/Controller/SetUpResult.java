package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.Player;

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

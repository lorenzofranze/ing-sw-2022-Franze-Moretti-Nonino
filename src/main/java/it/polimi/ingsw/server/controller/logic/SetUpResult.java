package it.polimi.ingsw.server.controller.logic;

/**
 * Useful to give to the Pianification phase the informations about the firstRandomPianificationPlayer calculated in the
 * previous SetUpPhase
 */

import it.polimi.ingsw.server.model.Player;

public class SetUpResult {
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

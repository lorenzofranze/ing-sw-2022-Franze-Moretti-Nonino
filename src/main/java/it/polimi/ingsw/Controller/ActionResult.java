package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;

public class ActionResult extends PhaseResult{

    private Player firstPianificationPlayer;
    private boolean finishedTowers;
    private boolean threeOrLessIslands;

    public Player getFirstPianificationPlayer() {
        return firstPianificationPlayer;
    }

    public void setFirstPianificationPlayer(Player firstPianificationPlayer) {
        this.firstPianificationPlayer = firstPianificationPlayer;
    }

    public boolean isFinishedTowers() {
        return finishedTowers;
    }

    public void setFinishedTowers(boolean finishedTowers) {
        this.finishedTowers = finishedTowers;
    }

    public boolean isThreeOrLessIslands() {
        return threeOrLessIslands;
    }

    public void setThreeOrLessIslands(boolean threeOrLessIslands) {
        this.threeOrLessIslands = threeOrLessIslands;
    }

    public void ActionResult(){
        firstPianificationPlayer = null;
        finishedTowers = false;
        threeOrLessIslands = false;
    }
}

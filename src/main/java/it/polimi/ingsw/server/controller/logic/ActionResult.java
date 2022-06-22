package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.server.model.Player;

public class ActionResult {

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

    public ActionResult(){
        firstPianificationPlayer = null;
        finishedTowers = false;
        threeOrLessIslands = false;
    }


}

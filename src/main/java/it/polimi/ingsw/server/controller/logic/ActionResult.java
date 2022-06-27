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

    @Override
    public boolean equals(Object o){
        if (o == null) {
            return false;
        }
        ActionResult o1;
        if (o instanceof ActionResult){
            o1 = (ActionResult) o;
        }else{
            return false;
        }
        if (firstPianificationPlayer != null){
            if (o1.firstPianificationPlayer != null){
                if (!firstPianificationPlayer.getNickname().equals(o1.firstPianificationPlayer.getNickname())){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.firstPianificationPlayer != null){
                return false;
            }
        }

        if (finishedTowers != o1.finishedTowers){
            return false;
        }
        if (threeOrLessIslands != o1.threeOrLessIslands){
            return false;
        }
        return true;
    }


}

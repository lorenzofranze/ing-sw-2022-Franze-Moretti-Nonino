package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Model.*;

public class Card8 extends CharacterEffectInfluence{
    private final GameController gameController;

    public Card8(GameController gameController){
        this.gameController = gameController;
    }
    @Override
    /**During the influence calculation this turn, you count as having 2 more influence*/
    public Player effectInfluence(Island island) {
        if(island.getHasNoEntryTile()){
            island.setHasNoEntryTile(false);
            return null;
        }

        Game game = this.gameController.getGame();
        Player moreInfluentPlayer = null;

        int maxInfluence=0;
        int currScore;
        for(Player p: game.getPlayers()) {
            currScore = 0;
            if (p.equals(gameController.getCurrentPlayer())){
                currScore += 2;
            }
            for (ColourPawn professor : ColourPawn.values()) {
                if(p.getSchoolBoard().getProfessors().get(professor)==1)
                    currScore += island.getStudents().get(professor);
            }
            if (p.getColourTower() == island.getTowerColour()) {
                currScore += island.getTowerCount();
            }

            if (currScore > 0 && currScore > maxInfluence) {
                maxInfluence = currScore;
                moreInfluentPlayer = p;
            }
        }

        return moreInfluentPlayer;
    }

    @Override
    public void doEffect() {

    }

}

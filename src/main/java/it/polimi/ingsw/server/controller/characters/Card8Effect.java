package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.*;

public class Card8Effect extends CharacterEffect{

    public Card8Effect(GameController gameController, CharacterState characterState) {
        super(gameController, characterState);
    }

    @Override
    /**
     * Calculates influence adding two more points to the score of current player
     */
    public Player effectInfluence(Island island) {

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

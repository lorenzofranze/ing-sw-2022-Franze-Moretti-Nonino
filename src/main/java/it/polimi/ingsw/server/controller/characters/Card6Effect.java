package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.*;

public class Card6Effect extends CharacterEffect{

    public Card6Effect(GameController gameController, CharacterState characterState) {
        super(gameController, characterState);
    }

    @Override
    /**
     * When resolving a Conquering on a Island, Towers do not count towards influence
     * */
    public Player effectInfluence(Island island) {

        Game game = this.gameController.getGame();
        Player moreInfluentPlayer = null;

        int maxInfluence=0;
        int currScore;
        for(Player p: game.getPlayers()) {
            currScore = 0;
            for (ColourPawn professor : ColourPawn.values()) {
                if(p.getSchoolBoard().getProfessors().get(professor)==1)
                    currScore += island.getStudents().get(professor);
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
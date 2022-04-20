package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Model.*;

import java.util.ArrayList;

public class Card6 extends CharacterEffectInfluence{

    private GameController gameController;

    public Card6(GameController gameController){
        this.gameController = gameController;
    }

    @Override
    /**When resolving a Conquering on a Island, Towers do not count towards influence*/
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
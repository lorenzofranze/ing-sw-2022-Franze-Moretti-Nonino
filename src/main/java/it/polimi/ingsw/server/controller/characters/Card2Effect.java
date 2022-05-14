package it.polimi.ingsw.server.controller.characters;


import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.PawnsMap;
import it.polimi.ingsw.server.model.Player;

public class Card2Effect extends CharacterEffect{
    private PawnsMap stolenProfessors;

    public Card2Effect(GameController gameController, CharacterState characterState){
        super(gameController, characterState);
        this.stolenProfessors = new PawnsMap();
    }


    public void doEffect(){
        for(Player p : gameController.getGame().getPlayers()){
            for(ColourPawn colour : ColourPawn.values()) {
                if (!(p.getSchoolBoard().equals(gameController.getCurrentPlayer().getSchoolBoard()))) {
                    if (!(p.getSchoolBoard().getProfessors().get(colour) == 0)
                            && gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().get(colour) >=
                            p.getSchoolBoard().getDiningRoom().get(colour)) {
                                stolenProfessors.add(colour);

                    }
                }
            }
        }
    }

    public Player effectInfluence(Island island) {
        Player moreInfluent=null;
        int maxInfluence=0;
        int currScore;
        for(Player p: gameController.getGame().getPlayers()) {
            currScore = 0;
            for (ColourPawn professor : ColourPawn.values()) {
                if(p.equals(gameController.getCurrentPlayer()) &&
                        (p.getSchoolBoard().getProfessors().get(professor)==1 || stolenProfessors.get(professor) == 1) )
                    currScore += island.getStudents().get(professor);
                else if(! p.equals(gameController.getCurrentPlayer()) && p.getSchoolBoard().getProfessors().get(professor)==1
                && !(stolenProfessors.get(professor) == 1)){
                    currScore += island.getStudents().get(professor);
                }
            }
            if (p.getColourTower() == island.getTowerColour()) {
                currScore += island.getTowerCount();
            }

            if (currScore > 0 && currScore > maxInfluence) {
                maxInfluence = currScore;
                moreInfluent = p;
            }
        }

        return moreInfluent;
    }
}


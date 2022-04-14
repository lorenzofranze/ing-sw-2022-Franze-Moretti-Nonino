package it.polimi.ingsw.server.Controller.Characters;


import it.polimi.ingsw.server.Controller.GameController;
import it.polimi.ingsw.server.Controller.Messages.MessageHandler;
import it.polimi.ingsw.server.Model.ColourPawn;
import it.polimi.ingsw.server.Model.Island;
import it.polimi.ingsw.server.Model.PawnsMap;
import it.polimi.ingsw.server.Model.Player;

public class Card2 implements CharacterEffect {

    private final GameController gameController;
    private PawnsMap stolenProfessors;

    public Card2(GameController gameController){
        this.gameController = gameController;
        this.stolenProfessors = new PawnsMap();
    }

    public void initializeCard() {
        //empty
    }

    public void doEffect(){
        for(Player p : gameController.getGame().getPlayers()){
            for(ColourPawn colour : ColourPawn.values()) {
                if (!(p.getSchoolBoard().equals(gameController.getCurrentPlayer().getSchoolBoard()))) {
                    if (!(p.getSchoolBoard().getProfessors().get(colour) == 0)
                            && gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().get(colour) >=
                            p.getSchoolBoard().getDiningRoom().get(colour)) {
                                stolenProfessors.add(colour);
                        //to user: now you control also theese professors: print stolenProfessors
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


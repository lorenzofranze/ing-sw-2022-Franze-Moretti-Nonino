package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.PawnsMap;
import it.polimi.ingsw.Server.Model.Player;

public class Card3 {
    private final GameController gameController;
    private Island island;


    public Card3(GameController gameController, Island island){
        this.gameController = gameController;
        this.island=island;
    }

    public void initializeCard() {
        //empty
    }



    /** calculates the influence on the chosen tower and, if some tower have to be added or removed from the island,
     * the method move correctly the towers from the players' schoolboard to the island and viceversa
     */
    public void doEffect(){
        Player moreInfluent=island.getInfluence(gameController.getGame());
        if(island.getTowerCount()==0){
            island.setTowerColor(moreInfluent.getColourTower());
            island.addTower(1);
            moreInfluent.getSchoolBoard().removeTower(1);
        }
        else if(island.getTowerColour()!=moreInfluent.getColourTower()){
            for(Player p: gameController.getGame().getPlayers()){
                if(p.getColourTower()==island.getTowerColour()){
                    p.getSchoolBoard().addTower(island.getTowerCount());
                }
            }
            island.setTowerColor(moreInfluent.getColourTower());
            moreInfluent.getSchoolBoard().removeTower(island.getTowerCount());

        }
    }
}

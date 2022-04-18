package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.Player;

public class Card4 implements CharacterEffect{
    private final GameController gameController;


    public Card4(GameController gameController){
        this.gameController = gameController;
    }

    public void initializeCard() {
        //empty
    }

    /**
     * During the actionPhase the the player can choose to move
     * mother island to steps more than normally permitted.
     */
    public void doEffect(){
        gameController.addTwoMovements(gameController.getCurrentPlayer());
    }

}

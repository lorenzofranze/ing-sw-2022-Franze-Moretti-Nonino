package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.server.controller.logic.GameController;

public class Card4 extends CharacterEffect{
    private final GameController gameController;


    public Card4(GameController gameController){
        this.gameController = gameController;
    }


    /**
     * During the actionPhase the the player can choose to move
     * mother island to steps more than normally permitted.
     */
    public void doEffect(){
        gameController.addTwoMovements(gameController.getCurrentPlayer());
    }

}

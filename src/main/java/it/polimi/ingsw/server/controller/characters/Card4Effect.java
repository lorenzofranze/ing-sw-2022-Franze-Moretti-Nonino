package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public class Card4Effect extends CharacterEffect{

    public Card4Effect(GameController gameController, CharacterState characterState){
        super(gameController, characterState);
    }


    /**
     * During the actionPhase the the player can choose to move
     * mother island to steps more than normally permitted.
     */
    public void doEffect(){
        gameController.addTwoMovements(gameController.getCurrentPlayer());
    }

    @Override
    public Player effectInfluence(Island island) {
        return null;
    }

}

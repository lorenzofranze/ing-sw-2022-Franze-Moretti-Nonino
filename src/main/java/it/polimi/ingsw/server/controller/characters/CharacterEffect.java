package it.polimi.ingsw.server.controller.characters;


import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public abstract class CharacterEffect {
    protected GameController gameController;
    protected CharacterState characterState;

    /**
     * The method will be called when the player decides to use the effect and implements the
     * card's effect */
    public abstract void doEffect();

    /**
     * For all charcter cards that calculates influence in a different way */
    public abstract Player effectInfluence(Island island);

    public CharacterEffect(GameController gameController, CharacterState characterState){
        this.gameController = gameController;
        this.characterState = characterState;
    }

    public int getID(){
        return characterState.getCharacterId();
    }
}

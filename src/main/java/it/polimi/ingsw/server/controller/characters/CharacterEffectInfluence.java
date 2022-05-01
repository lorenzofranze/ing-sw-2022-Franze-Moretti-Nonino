package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public abstract class CharacterEffectInfluence extends CharacterEffect {

    /** for all charcter cards that calculates influence in a different way */
    public abstract Player effectInfluence(Island island);

}

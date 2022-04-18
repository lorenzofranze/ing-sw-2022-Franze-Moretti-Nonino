package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.Player;

public abstract class CharacterEffectInfluence extends CharacterEffect {

    /** for all charcter cards that calculates influence in a different way */
    public abstract Player effectInfluence(Island island);

}

package it.polimi.ingsw.Server.Controller.Characters;


import it.polimi.ingsw.Server.Model.*;

public abstract class CharacterEffect {
    /* tutte le sottoclassi devono implementare questi metodi che sono comportamenti comuni a quasi tutte
    *  le carti, in caso contrario lasciano vouto il metodo. Possono ovviamente aggiungere altri attributi
    *  e metodi
     */

    /** the method will be called when the player decides to use the effect and implements the
     * card's effect */
    public abstract void doEffect();


}

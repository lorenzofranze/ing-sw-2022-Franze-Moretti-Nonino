package it.polimi.ingsw.server.Controller.Characters;


import it.polimi.ingsw.server.Model.*;

public interface CharacterEffect {
    /* tutte le sottoclassi devono implementare questi metodi che sono comportamenti comuni a quasi tutte
    *  le carti, in caso contrario lasciano vouto il metodo. Possono ovviamente aggiungere altri attributi
    *  e metodi
     */

    /** the method is called when the character card is choosen; it initializes the card if necessary
     *  (e.g place 4 students on the card)
     */
    public void initializeCard();

    /** the method will be called when the player decides to use the effect and implements the
     * card's effect */
    public void doEffect();

    /** for all charcter cards that calculates influence in a different way */
    public Player effectInfluence(Island island);


}

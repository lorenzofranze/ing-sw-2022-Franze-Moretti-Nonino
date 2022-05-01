package it.polimi.ingsw.server.controller.characters;

public abstract class CharacterEffectInitialize extends CharacterEffect {

    /** the method is called when the character card is choosen; it initializes the card if necessary
     *  (e.g place 4 students on the card)
     */
    public abstract void initializeCard();

}

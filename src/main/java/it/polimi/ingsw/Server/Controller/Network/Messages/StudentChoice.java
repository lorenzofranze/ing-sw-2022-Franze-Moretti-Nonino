package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Model.ColourPawn;

/** used by Card11 and 12
 *
 */
public class StudentChoice extends  ClientMessage{
    private ColourPawn colourPawn;
    public ColourPawn getColourPawn() {
        return colourPawn;
    }

}

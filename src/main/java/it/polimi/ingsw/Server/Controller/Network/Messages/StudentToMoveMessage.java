package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.PawnsMap;

public class StudentToMoveMessage extends ClientMessage {
    private ColourPawn colourPawn;
    private Integer where;


    public Integer getWhere() {
        return where;
    }

    public ColourPawn getColourPawn() {
        return colourPawn;
    }


}

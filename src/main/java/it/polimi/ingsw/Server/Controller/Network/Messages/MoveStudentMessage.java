package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.PawnsMap;

public class MoveStudentMessage extends ClientMessage {
    private ColourPawn studentToMove;
    private Integer where;


    public Integer getWhere() {
        return where;
    }

    public ColourPawn getStudentsToMove() {
        return studentToMove;
    }
}

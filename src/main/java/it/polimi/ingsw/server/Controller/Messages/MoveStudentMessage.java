package it.polimi.ingsw.server.Controller.Messages;

import it.polimi.ingsw.server.Model.PawnsMap;

public class MoveStudentMessage extends ClientMessage  {
    private PawnsMap studentsToMove;
    private Integer where;
    private GameState gameState;

    public GameState getGameState() {
        return gameState;
    }

    public Integer getWhere() {
        return where;
    }

    public PawnsMap getStudentsToMove() {
        return studentsToMove;
    }
}

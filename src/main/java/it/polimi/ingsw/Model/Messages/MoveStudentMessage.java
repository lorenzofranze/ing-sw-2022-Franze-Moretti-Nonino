package it.polimi.ingsw.Model.Messages;

import it.polimi.ingsw.Model.PawnsMap;

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

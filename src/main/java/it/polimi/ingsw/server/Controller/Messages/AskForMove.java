package it.polimi.ingsw.server.Controller.Messages;

public class AskForMove extends ServerMessage{
    private TypeOfMove typeOfMove;

    public TypeOfMove getTypeOfMove() {
        return typeOfMove;
    }
}

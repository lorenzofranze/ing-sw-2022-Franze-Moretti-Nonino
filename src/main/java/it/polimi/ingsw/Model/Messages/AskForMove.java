package it.polimi.ingsw.Model.Messages;

public class AskForMove extends ServerMessage{
    private TypeOfMove typeOfMove;

    public TypeOfMove getTypeOfMove() {
        return typeOfMove;
    }
}

package it.polimi.ingsw.server.Controller.Network.Messages;

public class AskForMove extends ServerMessage {
    private TypeOfMove typeOfMove;

    public TypeOfMove getTypeOfMove() {
        return typeOfMove;
    }
}

package it.polimi.ingsw.server.Controller.Network.Messages;

public class ErrorMessage extends ServerMessage{
    public TypeOfError typeOfError;

    public TypeOfError getTypeOfError() {
        return typeOfError;
    }
}

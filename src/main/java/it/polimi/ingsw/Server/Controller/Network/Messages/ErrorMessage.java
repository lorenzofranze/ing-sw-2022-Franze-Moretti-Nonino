package it.polimi.ingsw.Server.Controller.Network.Messages;

public class ErrorMessage extends ServerMessage{
    public TypeOfError typeOfError;

    public TypeOfError getTypeOfError() {
        return typeOfError;
    }
}

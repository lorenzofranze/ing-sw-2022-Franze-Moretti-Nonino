package it.polimi.ingsw.Model.Messages;

public class ErrorMessage extends ServerMessage{
    public TypeOfError typeOfError;

    public TypeOfError getTypeOfError() {
        return typeOfError;
    }
}

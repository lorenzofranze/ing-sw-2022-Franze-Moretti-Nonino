package it.polimi.ingsw.Model.Messages;

public enum TypeOfError {
    WrongMove(1);
    private int numOfError;

    private TypeOfError(int i) {
        numOfError=i;
    }
}

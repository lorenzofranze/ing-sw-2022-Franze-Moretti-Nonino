package it.polimi.ingsw.server.Controller.Network.Messages;

public enum TypeOfError {
    WrongMove(1), OnePlayerDisconnectedPermanently(2);
    private int numOfError;

    private TypeOfError(int i) {
        numOfError=i;
    }
}

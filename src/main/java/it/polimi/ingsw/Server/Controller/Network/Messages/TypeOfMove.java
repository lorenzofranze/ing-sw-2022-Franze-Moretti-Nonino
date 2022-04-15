package it.polimi.ingsw.Server.Controller.Network.Messages;

public enum TypeOfMove {
    PianificationMove(1), ActionMove(2), ChooseCloudMove(3);
    private int numOfMove;

    private TypeOfMove(int i) {
        numOfMove=i;
    }
}

package it.polimi.ingsw.common.messages;

public enum TypeOfAck {
    CorrectConnection(0), CompleteLobby(1), CorrectMove(2);

    private int messageCode;
    private TypeOfAck(int i) {
        messageCode=i;
    }
}

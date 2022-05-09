package it.polimi.ingsw.common.messages;

public enum TypeOfAsync {
    Disconnection(0);

    private int messageCode;
    private TypeOfAsync(int i) {
        messageCode=i;
    }
}

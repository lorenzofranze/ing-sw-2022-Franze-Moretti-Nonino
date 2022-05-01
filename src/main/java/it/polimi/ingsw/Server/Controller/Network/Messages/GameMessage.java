package it.polimi.ingsw.Server.Controller.Network.Messages;

public class GameMessage extends ClientMessage{
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

package it.polimi.ingsw.Model.Messages;


public abstract class ClientMessage {
    private ClientMessageHeader header;

    public ClientMessageHeader getHeader() {
        return header;
    }
}

package it.polimi.ingsw.Server.Controller.Network.Messages;

public class ErrorGameMessage extends ServerMessage{
    private String errorString;
    public ErrorGameMessage(TypeOfMessage typeOfMessage) {
        super(typeOfMessage);
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
}

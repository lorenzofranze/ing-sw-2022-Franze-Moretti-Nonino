package it.polimi.ingsw.Server.Controller.Network.Messages;

public class ErrorGameMessage extends ServerMessage{
    private String errorString;
    public ErrorGameMessage() {
        super(TypeOfMessage.Error);
    }
    public ErrorGameMessage(String errorString){
        super(TypeOfMessage.Error);
        this.errorString=errorString;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
}

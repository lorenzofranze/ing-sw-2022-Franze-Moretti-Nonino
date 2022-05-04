package it.polimi.ingsw.common.messages;

public class GameErrorMessage extends Message{
    public ErrorStatusCode getError() {
        return error;
    }

    public void setError(ErrorStatusCode error) {
        this.error = error;
    }

    private ErrorStatusCode error;

    public GameErrorMessage(ErrorStatusCode error) {
        super(TypeOfMessage.Error);
        this.error=error;
    }

}

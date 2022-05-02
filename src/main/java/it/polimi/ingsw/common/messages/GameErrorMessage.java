package it.polimi.ingsw.common.messages;

public class GameErrorMessage extends Message{
    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    private int error;   // sostituibile con enum. per vari tipi di errore

    public GameErrorMessage(int error) {
        super(TypeOfMessage.Error);
        this.error=error;
    }

}

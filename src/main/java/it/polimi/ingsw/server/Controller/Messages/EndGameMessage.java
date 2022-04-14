package it.polimi.ingsw.server.Controller.Messages;

public class EndGameMessage extends ServerMessage {
    private String winnerNickname;

    public String getWinnerNickname() {
        return winnerNickname;
    }
}

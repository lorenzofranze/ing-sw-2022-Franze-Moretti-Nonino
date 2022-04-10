package it.polimi.ingsw.Model.Messages;

public class EndGameMessage extends ServerMessage {
    private String winnerNickname;

    public String getWinnerNickname() {
        return winnerNickname;
    }
}

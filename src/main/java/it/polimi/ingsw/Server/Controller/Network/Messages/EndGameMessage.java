package it.polimi.ingsw.Server.Controller.Network.Messages;

public class EndGameMessage extends ServerMessage {
    private String winnerNickname;

    public String getWinnerNickname() {
        return winnerNickname;
    }
}

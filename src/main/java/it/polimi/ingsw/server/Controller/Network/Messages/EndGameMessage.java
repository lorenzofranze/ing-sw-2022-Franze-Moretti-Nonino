package it.polimi.ingsw.server.Controller.Network.Messages;

public class EndGameMessage extends ServerMessage {
    private String winnerNickname;

    public String getWinnerNickname() {
        return winnerNickname;
    }
}

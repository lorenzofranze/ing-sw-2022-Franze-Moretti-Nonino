package it.polimi.ingsw.Server.Controller.Network.Messages;


public abstract class ServerMessage extends Message{
    private String nickname;
    private GameState gameState;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGameState(GameState gameState) {this.gameState= gameState;}
}

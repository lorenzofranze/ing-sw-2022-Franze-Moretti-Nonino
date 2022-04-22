package it.polimi.ingsw.Server.Controller.Network.Messages;


public class ServerMessage extends Message{
    private String nickname;
    private GameStateMessage gameStateMessage;

    public ServerMessage(String nickname, GameStateMessage gameStateMessage, TypeOfMessage typeOfMessage){
        this.nickname=nickname;
        this.gameStateMessage = gameStateMessage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGameState(GameStateMessage gameStateMessage) {this.gameStateMessage = gameStateMessage;}
}

package it.polimi.ingsw.Server.Controller.Network.Messages;


public class ServerMessage extends Message{
    private String nickname;

    public ServerMessage(String nickname, TypeOfMessage typeOfMessage){
        this.nickname=nickname;
        super.setMessageType(typeOfMessage);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

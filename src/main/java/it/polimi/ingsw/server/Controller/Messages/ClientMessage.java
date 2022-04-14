package it.polimi.ingsw.server.Controller.Messages;


public abstract class ClientMessage extends Message{
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

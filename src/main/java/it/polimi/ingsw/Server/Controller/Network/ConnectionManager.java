package it.polimi.ingsw.Server.Controller.Network;

import java.net.Socket;

public class ConnectionManager{
    private Lobby lobby;
    private MessageHandler messageHandler;

    public ConnectionManager(Lobby lobby, MessageHandler messageHandler){
        this.lobby=lobby;
        this.messageHandler=messageHandler;
    }

    public void startPing(){
        for(String s: lobby.getUsersNicknames()){
            Socket socket= lobby.getUsersReadyToPlay().get(s);
            PingSender pingSender=new PingSender(s, messageHandler);
            pingSender.run();
        }
    }
}

/*
package it.polimi.ingsw.Server.Controller.Network;


import java.io.IOException;
import java.net.Socket;

public class ConnectionManager{
    private Lobby lobby;
    private MessageHandler messageHandler;
    private boolean active;

    public ConnectionManager(Lobby lobby, MessageHandler messageHandler){
        this.lobby=lobby;
        this.messageHandler=messageHandler;
    }


    public void closeConnection(){
        System.out.println("Connection closed from the server side");
        try{
            for(Socket socket: lobby.getUsersReadyToPlay().values())
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        active = false;
    }


    /*public void startPing(){
        for(String s: lobby.getUsersNicknames()){
            Socket socket= lobby.getUsersReadyToPlay().get(s);
            PingSender pingSender=new PingSender(s, messageHandler);
            pingSender.run();
        }
    }*/
/*
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
        */

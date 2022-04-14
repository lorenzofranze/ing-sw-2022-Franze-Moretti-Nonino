package it.polimi.ingsw.server.Controller.Network.Messages;

import it.polimi.ingsw.server.Controller.Network.JsonConverter;
import it.polimi.ingsw.server.Controller.Network.LobbyManager;
import it.polimi.ingsw.server.Controller.Network.MessageHandler;

import java.io.IOException;

public class PingSender implements Runnable{
    private boolean isConnected;
    private final static int PING_TIMEOUT= 125000;
    private String nickname;
    private MessageHandler messageHandler;

    public PingSender(String nickname, MessageHandler messageHandler){
        this.nickname=nickname;
        isConnected=true;
    }

    //CHI USA QUESTA FUNZIONE??
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public void run() {
        while(isConnected){
            PingMessage message= new PingMessage();
            message.setMessageType("PingMessage");
            JsonConverter jsonConverter=new JsonConverter();
            String messageString= jsonConverter.fromMessageToJson(message);

            this.isConnected=false;
            try {
                messageHandler.getBufferedReaderOut().get(nickname).write(messageString);
                messageHandler.getBufferedReaderOut().get(nickname).flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LobbyManager lobbyManager=LobbyManager.getInstance();
        lobbyManager.addDisconnectedPlayers(nickname);

        //SE ARRIVO QUI è DISCONNESSO!?!?!?!

    }
}
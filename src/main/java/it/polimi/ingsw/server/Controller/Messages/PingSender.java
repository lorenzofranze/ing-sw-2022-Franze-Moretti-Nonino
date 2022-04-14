package it.polimi.ingsw.server.Controller.Messages;

import java.io.IOException;

public class PingSender implements Runnable{
    private boolean isConnected;
    private final static int PING_TIMEOUT= 125000;
    private String nickname;
    private MessageHandler messageHandler;

    public PingSender(String nickname, MessageHandler messageHandler){
        this.nickname=nickname;
        this.messageHandler=messageHandler;
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
            JsonConverter jsonConverter=JsonConverter.getJsonConverterInstance();
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
        //SE ARRIVO QUI è DISCONNESSO!?!?!?!
        //cambio message manager NAME UNIVOCITY : chiamo un nuovo metodo che si aspetta il nickname del giocatore sconnesso
        //inizia un timeout per attendere la riconnessione

    }
}

package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.network.PlayerManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.common.messages.TypeOfMessage.*;

public class NetworkHandler implements Runnable{

    private String serverIp;
    private int serverPort;
    private Socket socket;
    private PingSenderFromClient pingSenderFromClient;
    private LinkedBlockingQueue<Message> messageQueue;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean isConnected;

    private JsonConverter jsonConverter = new JsonConverter();
    private static Thread pingSenderFromClientThread;

    public NetworkHandler(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.messageQueue= new LinkedBlockingQueue<>();
    }

    public void connectToServer() throws IOException {
        this.socket = new Socket(serverIp, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        isConnected=true;
        pingSenderFromClient=new PingSenderFromClient(this);
        pingSenderFromClientThread= new Thread(pingSenderFromClient);

        pingSenderFromClientThread.start();
    }

    /**
     * read from network buffer
     */
    private Message readFromBuffer() {

        String lastMessage = "";

        try {
            String line = in.readLine();
            while (!("EOF").equals(line)) {
                lastMessage = lastMessage + line + "\n";
                line = in.readLine();
            }
        } catch (IOException e) {
            if(!ClientController.getInstance().isDisconnected()){
                ClientController.getInstance().setDisconnected();
                System.out.println("ERROR-ClientMessageHandler-readFromBuffer");
                return null;
            }

        }

        Message receivedMessage = jsonConverter.fromJsonToMessage(lastMessage);

        /*
        if (receivedMessage.getMessageType()!=Update) {                             //DA CANCELLARE
            System.out.println("\n\nreceived from server: "+lastMessage+"\n\n");    //DA CANCELLARE
        }else{                                                                      //DA CANCELLARE
            System.out.println("\n\nreceived from server: \nupdate\n\n");           //DA CANCELLARE
        }                                                                           //DA CANCELLARE
        */

        if(receivedMessage==null){
            return null;
        }
        if (    (receivedMessage.getMessageType() == Update)       ||
                (receivedMessage.getMessageType() == Ack)       ||
                (receivedMessage.getMessageType() == Error)     ||
                (receivedMessage.getMessageType() == Async)     ||
                (receivedMessage.getMessageType() == Game)          ){
                    messageQueue.add(receivedMessage);
        }
        switch(receivedMessage.getMessageType()){
            case Async:
                System.out.println("Ti stai disconnettendo perchè hai ricevuto un async message");
                ClientController.getInstance().setDisconnected();
                AsyncMessage realAsyncMessage = (AsyncMessage) receivedMessage;
                break;
            case Ping:
                try {
                    sendToServer(new PongMessage());
                    System.out.println("invio Ping");
                } catch (IOException e) {
                    if(!ClientController.getInstance().isDisconnected()) {
                        ClientController.getInstance().setDisconnected();
                        System.out.println("ERROR-ClientMessageHandler-1");
                    }
                }
                break;
            case Pong:
                System.out.println("invio Pong");
                setPingConnected(true);
                break;
        }
        return receivedMessage;
    }

    public void sendToServer(Message message) throws IOException {
        if(ClientController.getInstance().isDisconnected()) return;
        Message messageToSend = message;
        if (message.getMessageType() == TypeOfMessage.Async) {
            AsyncMessage asyncMessage = (AsyncMessage) message;
            asyncMessage.setDescription("disconnection message sent from " + ClientController.getInstance().getNickname());
            messageToSend = asyncMessage;
        }
        String stringMessage = JsonConverter.fromMessageToJson(messageToSend);
        //System.out.println("sent to server:\n" + stringMessage);
        out.write(stringMessage);
        out.flush();
    }

    /*
    public int reconnect(String nickname, GameMode gameMode) {
        ConnectionMessage cm = new ConnectionMessage(nickname, gameMode);
        String stringToSend = JsonConverter.fromMessageToJson(cm);
        try {
            out.write(stringToSend);
            out.flush();
        } catch (IOException e) {
            System.out.println("ERROR-ClientMessageHandler-reconnect");
            return 0;
        }
        return 1;
    }
    */

    public void endClient() {
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR-ClientMessageHandler-endclient");
        }
    }

    public Message getReceivedMessage() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            if(!ClientController.getInstance().isDisconnected()) {
                ClientController.getInstance().setDisconnected();
                System.out.println("NetworkHandeler: Error -" +
                        " interruption the function take() in getReceivedMessage");
                //e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void run(){

        while(!ClientController.getInstance().isDisconnected()) {
            Message receivedMessage = this.readFromBuffer();
        }
    }

    public BufferedReader getIn() {
        return in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public Thread getPingSenderFromClientThread() {
        return pingSenderFromClientThread;
    }


    public boolean isPingConnected() {
        return isConnected;
    }

    public void setPingConnected(boolean connected) {
        isConnected = connected;
    }
}


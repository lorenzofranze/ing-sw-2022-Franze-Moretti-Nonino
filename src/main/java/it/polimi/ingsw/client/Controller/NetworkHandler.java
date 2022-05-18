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
        pingSenderFromClient=new PingSenderFromClient();
        pingSenderFromClientThread= new Thread(pingSenderFromClient);

        pingSenderFromClientThread.start();
    }

    /**
     * read from network buffer
     */
    private String readFromBuffer() {

        String lastMessage = "";

        try {
            String line = in.readLine();
            while (!("EOF").equals(line)) {
                lastMessage = lastMessage + line + "\n";
                line = in.readLine();
            }
        } catch (IOException e) {
            System.out.println("ERROR-ClientMessageHandler-readFromBuffer");
        }
        return lastMessage;
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
        System.out.println("sent to server:\n" + stringMessage);
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
        }
    }

    public Message getReceivedMessage() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            ClientController.getInstance().setDisconnected();
        }
        return null;
    }

    @Override
    public void run(){

        while(true) {

            String stringMessage = this.readFromBuffer();
            Message receivedMessage = jsonConverter.fromJsonToMessage(stringMessage);

            if (receivedMessage.getMessageType()==Update) {
                UpdateMessage realMessage = (UpdateMessage) receivedMessage;
                messageQueue.add(realMessage);
            }
            else if(receivedMessage.getMessageType()==Async) {
                System.out.println("\nreceived from server:\n" + stringMessage + "\n");
                ClientController.getInstance().setDisconnected();
                AsyncMessage realAsyncMessage = (AsyncMessage) receivedMessage;
                break;
            }
            else if(receivedMessage.getMessageType()==Ping) {

                System.out.println("\nreceived from server:\nping\n");
                try {
                    sendToServer(new PongMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else if(receivedMessage.getMessageType()==Pong) {
                System.out.println("\nreceived from server:\npong\n");
                pingSenderFromClient.setConnected(true);
            }
            else if(receivedMessage.getMessageType()==Ack){
                messageQueue.add(receivedMessage);
            }
            else if(receivedMessage.getMessageType()==Error){
                messageQueue.add(receivedMessage);
            }
            else if(receivedMessage.getMessageType()==Game)
            {
                messageQueue.add(receivedMessage);
            }
            else{
                messageQueue.add(receivedMessage);
            }
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
}


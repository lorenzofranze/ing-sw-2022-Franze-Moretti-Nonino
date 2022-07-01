package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.client.View.GUI.GuiController;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.network.PlayerManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.common.messages.TypeOfMessage.*;

/**
 * With connectToServer creates the input and output buffers and starts the ping thread
 * As long as there is no disconnection, it calls readFromBuffer() so that it can add messages to the queue
 * (or handle them without adding them to the queue in the case of Ping, Pong, Async)
 * as soon as they arrive on the buffer.
 * Other classes can call getReceivedMessage() to take the last message in the queue.
 * Sends the messages passed as a parameter to the server.
 * EndClient() is called by ClientApp when you need to close the client: it closes the input and output buffers.
 */
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

    /**
     * Creates the input and output buffers and starts the ping thread (being called immediately by the
     * clientApp, the ping also works while connecting with the server)
     * @throws IOException
     */
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
     * Reads strings from the buffer and turns them into messages with Json,
     * if they are of type Update, Ack, Error, Async, Game, adds them to the message queue.
     * otherwise.
     * -- if it is Ping, sends the Pong;
     * -- if it is Pong, set connected of the PingSender to true;
     * -- if it is Async call ClientController.setDisconnected();
     * also in case of errors calls ClientController.setDisconnected().
     */
    private void readFromBuffer() {

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
                return;
            }

        }

        Message receivedMessage = jsonConverter.fromJsonToMessage(lastMessage);

        //UNCOMMENT IF YOU WANT TO SEE MESSAGES RECEIVED PRINTED (CLIENT SIDE)
        /*
        if (receivedMessage.getMessageType()!=Update && receivedMessage.getMessageType()!=Ping && receivedMessage.getMessageType()!=Pong) {                             //DA CANCELLARE
            System.out.println("\n\nreceived from server: "+lastMessage+"\n\n");
        }else if (receivedMessage.getMessageType()==Update){
            System.out.println("\n\nreceived from server: \nupdate\n\n");
        }
        */

        if(receivedMessage==null){
            return;
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
                System.out.println("Async message received: disconnecting");
                ClientController.getInstance().setDisconnected();
                AsyncMessage realAsyncMessage = (AsyncMessage) receivedMessage;
                GuiController.setOtherPlayerDisconnected(true); //if async message received show alert fot other player disconnected
                return;
            case Ping:
                try {
                    sendToServer(new PongMessage());
                } catch (IOException e) {
                    if(!ClientController.getInstance().isDisconnected()) {
                        ClientController.getInstance().setDisconnected();
                    }
                }
                break;
            case Pong:
                setPingConnected(true);
                break;
        }
        return;
    }

    /**
     * Sends the messages passed as a parameter to the server
     * @param message
     * @throws IOException
     */
    public void sendToServer(Message message) throws IOException {
        if(ClientController.getInstance().isDisconnected()) return;
        Message messageToSend = message;
        if (message.getMessageType() == TypeOfMessage.Async) {
            AsyncMessage asyncMessage = (AsyncMessage) message;
            asyncMessage.setDescription("disconnection message sent from " + ClientController.getInstance().getNickname());
            messageToSend = asyncMessage;
        }
        String stringMessage = JsonConverter.fromMessageToJson(messageToSend);
        out.write(stringMessage);
        out.flush();
    }



    /**
     * Is called by ClientApp when you need to close the client.
     * Closes the input and output buffers.
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

    /**
     * Takes and returns the messages added to the queue by readFromBuffer().
     * In case the queue is empty, the take() method waits for it to be filled with a message.
     * @return
     */
    public Message getReceivedMessage() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            if(!ClientController.getInstance().isDisconnected()) {
                ClientController.getInstance().setDisconnected();
                System.out.println("NetworkHandeler: Error -" +
                        " interruption the function take() in getReceivedMessage");
            }
        }
        return null;
    }

    /**
     * As long as there is no disconnection, I call readFromBuffer() so that I can add messages to the queue
     * (or handle them without adding them to the queue in the case of Ping, Pong, Async)
     * as soon as they arrive on the buffer
     */
    @Override
    public void run(){
        while(!ClientController.getInstance().isDisconnected()) {
            this.readFromBuffer();
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


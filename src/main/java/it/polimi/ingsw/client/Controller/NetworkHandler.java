package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.network.PlayerManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static it.polimi.ingsw.common.messages.TypeOfMessage.Ping;

public class NetworkHandler {

    private String serverIp;
    private int serverPort;
    private Socket socket;

    private BufferedReader in;
    private BufferedWriter out;

    private JsonConverter jsonConverter = new JsonConverter();

    public NetworkHandler(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void connectToServer() throws IOException {
        this.socket = new Socket(serverIp, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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

    public void endClient() {
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message getReceivedMessage() {
        String stringMessage = this.readFromBuffer();
        Message receivedMessage = jsonConverter.fromJsonToMessage(stringMessage);

        switch (receivedMessage.getMessageType()) {
            case Update:
                System.out.println("\nreceived from server:\nupdate\n");
                UpdateMessage realMessage= (UpdateMessage) receivedMessage;
                break;
            case Async:
                System.out.println("\nreceived from server:\n" + stringMessage + "\n");
                ClientController.getInstance().setDisconnected();
                AsyncMessage realAsyncMessage= (AsyncMessage) receivedMessage;
                return realAsyncMessage;
            case Ping:
                System.out.println("\nreceived from server:\nping\n");
                try {
                    sendToServer(receivedMessage);
                    pingController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                receivedMessage = getReceivedMessage();
                break;
            default:
                System.out.println("\nreceived from server:\n" + stringMessage + "\n");
                break;
        }
        return receivedMessage;
    }

    public BufferedReader getIn() {
        return in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void pingController() {
        try {
            this.socket.setSoTimeout(60000);
        } catch (SocketException ex) {
            ex.printStackTrace();
            endClient();
            System.out.println("Il server non è più connesso");
            ClientController.getInstance().setDisconnected();

        }
    }
}


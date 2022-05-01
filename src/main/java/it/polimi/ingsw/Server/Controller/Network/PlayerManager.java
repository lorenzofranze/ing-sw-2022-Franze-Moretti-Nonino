package it.polimi.ingsw.Server.Controller.Network;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.Messages.*;
import it.polimi.ingsw.Server.Model.Player;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerManager implements Runnable{
    private Queue<ClientMessage> messageQueue;
    private JsonConverter jsonConverter;
    private String playerNickname;
    private BufferedReader bufferedReaderIn;
    private BufferedWriter bufferedReaderOut;
    private boolean isMyTurn=false;

    public PlayerManager(String playerNickname, BufferedReader bufferedReaderIn, BufferedWriter bufferedReaderOut) {
        this.playerNickname = playerNickname;
        this.bufferedReaderIn=bufferedReaderIn;
        this.bufferedReaderOut=bufferedReaderOut;

        this.jsonConverter= new JsonConverter();
    }

    //called in messageHandler

    /** this function receives the messages from the client:
     * if it is not the client turn, it sends to the client an error message and do not save the message,
     * if it is the client turn, the message is put in the FIFO Queue
     */
    public void run(){
        String receivedString;
        ClientMessage receivedMessage;

        while (true) {
            receivedString = readFromBuffer();
            if(isMyTurn==false){
                stringMessageToClient("It is not your turn");
            }
            else
            {
                receivedMessage = (ClientMessage) jsonConverter.fromJsonToMessage(receivedString);
                messageQueue.add(receivedMessage);
            }

        }
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public ClientMessage getLastMessage() {
        return messageQueue.poll();
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    private String readFromBuffer(){
        String lastMessage = "";

        try{
            String line = bufferedReaderIn.readLine();
            while (!line.equals("EOF")){
                lastMessage = lastMessage + line + "\n";
                line = bufferedReaderIn.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return lastMessage;
    }

    private boolean checkAnswerType(ServerMessage messageToSend, ClientMessage messageRecieved){
        if (messageToSend.getMessageType().equals(TypeOfMessage.GameState) && (messageRecieved.getMessageType().equals(TypeOfMessage.GameStateACK))){
            return true;
        }
        return  (messageToSend.getMessageType().equals(messageRecieved.getMessageType()));
    }

    public void stringMessageToClient(String stringToSend){
        StringMessage stringMessage= new StringMessage(stringToSend);
        String messageToSend = jsonConverter.fromMessageToJson(stringMessage);
        messageToSend = messageToSend + "\nEOF\n";
        try {
            bufferedReaderOut.write(messageToSend);
            bufferedReaderOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ServerMessage message){
        String stringToSend = jsonConverter.fromMessageToJson(message);
        try {
            bufferedReaderOut.write(stringToSend);
            bufferedReaderOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

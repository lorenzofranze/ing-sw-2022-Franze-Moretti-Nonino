package it.polimi.ingsw.server.controller.network;
/**
 * There is one PlayerManager for each player. Its function is to manage the messages arriving from the player
 * using a queue and to send the messages to that player.
 * The pingThread starts working even before the player chooses his nickname so that disconnections
 * can be detected also during the phases that come before the start of the game.
 * The method run() is a "while loop" that keeps looping until the game is set to stop or a disconnection has been detected.
 * run() calls readFromBuffer() and, according to the type of message read, it reacts:
 * - AsyncMessage --> disconnect players.
 * - PongMessage --> correct connection
 * - PingMessage --> answer with Pong
 * - Unexpected GameMessage --> answer with Error
 * - otherwise: add the message to the message queue.
 * When the GameController needs to read a message during one of its phases, it calls readMessage():
 * this method reads the last message of the queue: if it is of the type expected it return the message
 * otherwise it sends an errorGameMessage to the client and it reads the last message of the queue again
 * until it finds the message it is waiting for.
 * When other classes want to send a message to the player associated with the playerManager, they call
 * sendMessage(message) that converts the message into a jsonString using jsonConverter and then sends it.
 **/

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.ingsw.Server.Controller.Network.PingSender;

public class PlayerManager implements Runnable{
    private LinkedBlockingQueue<Message> messageQueue;
    //private boolean characterReceived=false;
    private JsonConverter jsonConverter;
    private String playerNickname;
    private BufferedReader bufferedReaderIn;
    private BufferedWriter bufferedReaderOut;
    private PingSender pingSender;
    private Thread pingThread;
    private Boolean connected;
    private transient boolean isMyTurn=false;
    private transient boolean toStop=false;
    private boolean closeConnectionBeenCalled;

    /**
     * Starts the pingThread.
     * The pingThread starts working even before the player chooses his nickname so that disconnections
     * can be detected also during the phases that come before the start of the game
     * @param bufferedReaderIn
     * @param bufferedReaderOut
     */
    public PlayerManager(BufferedReader bufferedReaderIn, BufferedWriter bufferedReaderOut) {
        this.playerNickname = " invalidNickname ";
        this.bufferedReaderIn=bufferedReaderIn;
        this.messageQueue=new LinkedBlockingQueue<>();
        this.bufferedReaderOut=bufferedReaderOut;
        this.closeConnectionBeenCalled=false;
        this.connected=true;
        this.jsonConverter= new JsonConverter();
        this.pingSender=new PingSender(this);
        this.pingThread= new Thread(pingSender);
        pingThread.start();
    }



    //called in messageHandler

    /**
     * This function receives the messages from the client:
     * if it is not the client turn, it sends to the client an error message and do not save the message,
     * if it is the client turn, the message is put in the FIFO Queue
     */
    @Override
    public void run(){
        String receivedString;
        Message receivedMessage;


        while (toStop!=true && !ClientController.getInstance().isDisconnected()) {
            receivedString = readFromBuffer();
            receivedMessage = jsonConverter.fromJsonToMessage(receivedString);
            if(receivedMessage==null) return;

            switch(receivedMessage.getMessageType()){
                case Async: //if i have received an async message(a disconnection message)
                    pingThread.interrupt();
                    if(toStop==false)
                    {
                        toStop=true;
                        if(!closeConnectionBeenCalled) {
                            closeConnectionBeenCalled=true;
                            ServerController.getInstance().closeConnection(playerNickname);
                        }
                    }
                    return;
                    //nota : ho tolto break
                case Pong:
                    setConnected(true);
                    break;
                case Ping:
                    PongMessage pongMessage=new PongMessage();
                    sendMessage(pongMessage);
                    break;
                case Game:
                    GameMessage gameMessage = (GameMessage) receivedMessage;
                    if (isMyTurn==false){
                        ErrorMessage errorMessage = new ErrorMessage(TypeOfError.TurnError);
                        sendMessage(errorMessage);
                    }else{
                        messageQueue.add(receivedMessage);
                    }
                    break;
                default:
                    //no action
                    break;
            }
        }
        if(pingThread.isInterrupted()==false){
            pingThread.interrupt();
        }

    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    /**
     * Returns the last message in the queue and removes it from the queue. If the queue is empty, waits until
     * a message is added.
     * @return
     */
    public Message getLastMessage() {
        Message message = null;
        boolean received = false;
        do{
            try {
                message =  messageQueue.take();
                received = true;
            } catch (InterruptedException e) {
                if(toStop==false)
                {
                    toStop=true;
                    if(!closeConnectionBeenCalled) {
                        closeConnectionBeenCalled=true;
                        ServerController.getInstance().closeConnection(playerNickname);
                        pingThread.interrupt();
                    }
                }
                e.printStackTrace();
            }
        }while(received == false);
        return message;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    /**
     * Reads the bufferReaderIn and returns the corresponding String
     * @return
     */
    private String readFromBuffer(){

        String lastMessage = "";
        //during tests
        if (bufferedReaderIn==null){return lastMessage;}

        try{
            String line = bufferedReaderIn.readLine();
            while (!("EOF").equals(line)){
                lastMessage = lastMessage + line + "\n";
                line = bufferedReaderIn.readLine();
            }
        } catch(IOException e){
            //e.printStackTrace();
            if(toStop==false)
            {
                toStop=true;
                if(pingThread.isInterrupted()==false){
                    pingThread.interrupt();
                }
                if(!closeConnectionBeenCalled) {
                    closeConnectionBeenCalled=true;
                    ServerController.getInstance().closeConnection(playerNickname);
                }
            }
            return null;
        }
        return lastMessage;
    }

    /**
     * Converts the message into a string using jsonConverter,
     * then sends it
     * @param message
     */
    public void sendMessage(Message message){
        String stringToSend = jsonConverter.fromMessageToJson(message);

        //null only in tests
        if (bufferedReaderOut == null){return;}

        try {
            bufferedReaderOut.write(stringToSend);
            bufferedReaderOut.flush();
        } catch (IOException e) {
            //e.printStackTrace();

            if(toStop==false) {
                toStop = true;
                if(pingThread.isInterrupted()==false){
                    pingThread.interrupt();
                }
                if(!closeConnectionBeenCalled){
                    closeConnectionBeenCalled=true;
                    ServerController.getInstance().closeConnection(playerNickname);
                }

            }
            return;
        }

    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * It reads the last message of the queue: if it is of the type expected it return the message
     * otherwise it sends an errorGameMessage to the client and it reads the last message of the queue again
     * until it finds the message it is waiting for
     * @param typeOfMessage
     * @return
     */
    public Message readMessage(TypeOfMessage expectedTypeOfMessage, Object specificTypeOfMessage) {
        Message receivedMessage;
        Boolean correctMatch;

        do {
            correctMatch = true;
            //this.setTimeout();
            receivedMessage = getLastMessage();


            if(receivedMessage.getMessageType().equals(expectedTypeOfMessage)){
                switch (receivedMessage.getMessageType()){
                    case Connection:
                        ConnectionMessage receivedMessageConnection = (ConnectionMessage) receivedMessage;
                        return receivedMessageConnection;

                    case Ack:
                        AckMessage receivedMessageAck = (AckMessage) receivedMessage;
                        if(specificTypeOfMessage.equals(receivedMessageAck.getTypeOfAck())){
                            return receivedMessageAck;
                        }else{
                            ErrorMessage errorMessage = new ErrorMessage(TypeOfError.UnmatchedMessages, "both Ack, different types" );
                            sendMessage(errorMessage);
                            correctMatch = false;
                        }
                        break;
                    case Update:
                        UpdateMessage receivedMessageUpdate = (UpdateMessage) receivedMessage;
                        return receivedMessageUpdate;
                    case Game:
                        GameMessage receivedMessageGame = (GameMessage) receivedMessage;
                        if(specificTypeOfMessage.equals(receivedMessageGame.getTypeOfMove())){
                            return receivedMessageGame;
                        }else{
                            ErrorMessage errorMessage = new ErrorMessage(TypeOfError.UnmatchedMessages,
                                    "both Game, different types. Requested " + specificTypeOfMessage.toString()+
                                    "; recived " + receivedMessageGame.getTypeOfMove());
                            sendMessage(errorMessage);
                            correctMatch = false;
                        }
                        break;
                    case Error:
                        ErrorMessage receivedMessageError = (ErrorMessage) receivedMessage;
                        if(specificTypeOfMessage.equals(receivedMessageError.getTypeOfError())){
                            return receivedMessageError;
                        }else{
                            ErrorMessage errorMessage = new ErrorMessage(TypeOfError.UnmatchedMessages, "both Error, different types");
                            sendMessage(errorMessage);
                            correctMatch = false;
                        }
                    case Ping:
                        PingMessage receivedMessagePing = (PingMessage) receivedMessage;
                        return receivedMessagePing;
                    case Pong:
                        PongMessage receivedMessagePong = (PongMessage) receivedMessage;
                        return receivedMessagePong;
                    case Async:
                        AsyncMessage receivedMessageAsync = (AsyncMessage) receivedMessage;
                        return receivedMessageAsync;
                }

            }else{
                ErrorMessage errorMessage = new ErrorMessage(TypeOfError.UnmatchedMessages, "expected: " + expectedTypeOfMessage.toString() + "\t received " + receivedMessage.getMessageType().toString());
                sendMessage(errorMessage);
                correctMatch = false;
            }
        } while (correctMatch==false);

        //questa riga di codice non dovrebbe mai essere eseguita, si dovrebbe sempre rientrare in uno dei casi precedenti
        return null;
    }

    public boolean isToStop() {
        return toStop;

    }

    public Thread getPingThread() {
        return pingThread;
    }

    public PingSender getPingSender() {
        return pingSender;
    }

    public Queue<Message> getMessageQueue() {
        return messageQueue;
    }

    public BufferedWriter getBufferedReaderOut() {
        return bufferedReaderOut;
    }
    public void setMessageQueue(LinkedBlockingQueue<Message> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public boolean getConnected() {
        boolean ris;
        synchronized( connected ) {
            ris = connected;
        }
        return ris;
    }

    public void setConnected(boolean connected) {
        synchronized(this.connected) {
            this.connected = connected;
        }
    }

    public boolean getCloseConnectionBeenCalled() {
        return closeConnectionBeenCalled;
    }

    public void setCloseConnectionBeenCalled(boolean closeConnectionBeenCalled) {
        this.closeConnectionBeenCalled = closeConnectionBeenCalled;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public BufferedReader getBufferedReaderIn() {
        return bufferedReaderIn;
    }
}

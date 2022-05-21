package it.polimi.ingsw.server.controller.network;

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

    public PlayerManager(String playerNickname, BufferedReader bufferedReaderIn, BufferedWriter bufferedReaderOut) {
        this.playerNickname = playerNickname;
        this.bufferedReaderIn=bufferedReaderIn;
        this.messageQueue=new LinkedBlockingQueue<>();
        this.bufferedReaderOut=bufferedReaderOut;
        this.closeConnectionBeenCalled=false;
        this.connected=true;
        /**todo: stop the thread**/
        this.jsonConverter= new JsonConverter();
    }

    public PlayerManager(String playerNickname){
        this.playerNickname = playerNickname;
        this.jsonConverter= new JsonConverter();
    }

    //called in messageHandler

    /** this function receives the messages from the client:
     * if it is not the client turn, it sends to the client an error message and do not save the message,
     * if it is the client turn, the message is put in the FIFO Queue
     */
    @Override
    public void run(){
        String receivedString;
        Message receivedMessage;
        this.pingSender=new PingSender(playerNickname, this);
        pingThread= new Thread(pingSender);
        pingThread.start();

        while (toStop!=true && !ClientController.getInstance().isDisconnected()) {
            receivedString = readFromBuffer();
            receivedMessage = jsonConverter.fromJsonToMessage(receivedString);
            if(receivedMessage==null) return;

            switch(receivedMessage.getMessageType()){
                case Async: //if i have received an async message(a disconnection message)
                    System.out.println(receivedString);
                    System.out.println("Player manager: received an Async message");
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
                        /*if (gameMessage.getTypeOfMove().equals(TypeOfMove.CharacterCard)){
                            System.out.println("FLAG - PLAYER MANAGER - SETTO CHARACTERRECEIVED = TRUE");
                            characterReceived=true;
                        }*/
                        messageQueue.add(receivedMessage);
                        //notifyAll();
                    }
                    break;
                default:
                    System.out.println(receivedString);
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
    /*public void setCharacterReceived(boolean characterReceived) {
        this.characterReceived = characterReceived;
    }
    public boolean isCharacterReceived() {
        return characterReceived;
    }*/
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

    private String readFromBuffer(){
        String lastMessage = "";

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
                System.out.println("Player manage: close connection 1");
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
                System.out.println("Player manager: error in sending message");
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

        //da cancellare fino a (fine cancella*)
        if (!message.getMessageType().equals(TypeOfMessage.Update)) {
            System.out.println("Message sent to: " + this.getPlayerNickname());
            System.out.println(stringToSend);
        }
        else{
            System.out.println("Message sent to: " + this.getPlayerNickname());
            System.out.println("update");
        }
        //(fine cancella *)
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * it reads the last message of the queue: if it is of the type expected it return the message
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

            System.out.println("Message received:\n"+jsonConverter.fromMessageToJson(receivedMessage));

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

    /**
     * stops the game if a player do not answer for more than 3 minutes
     * it is called in the readMessage method (in playerManager)
     * so it is called every thime the gameController asks to read the last message of the queue
     */
    /*
    public void setTimeout() {
        //nei test questo metodo non fa nulla
        if (ServerController.getInstance().getCurrentGames().isEmpty()){
            return;
        }


        //il turno del giocatore dura 3 minuti al massimo: se non risponde la partita finisce

        //cerco la lobby corrispondente a questa partita nel server controller: lo faccio perchè mi servono lo socket
        //dei giocatori

        Lobby lobby = null;
        MessageHandler messageHandler=null;
        for (GameController gameController : ServerController.getInstance().getCurrentGames().values()) {
            for (Player p : gameController.getGame().getPlayers()) {
                if (p.getNickname().equals(playerNickname)) {
                    lobby = gameController.getLobby();
                    messageHandler=gameController.getMessageHandler();
                }
            }
        }

        //mette il timeout alla socket del giocatore corrente si 2 minuti,
        //se non riceve nulla dal client in questo tempo si alza una timeout exception e
        //si avvisano gli altri giocatori che la partita si è conclusa
        Socket clientSocket;
        for (String s : lobby.getUsersReadyToPlay().keySet()) {
            if (s.equals(playerNickname)) {
                clientSocket = lobby.getUsersReadyToPlay().get(s);
                try {
                    clientSocket.setSoTimeout(120000);
                } catch (SocketException ex) {
                    System.out.println("the player"+playerNickname+ "is too slow! His round has exceeded 2 minutes!");
                    ex.printStackTrace();
                    AsyncMessage asyncMessage=new AsyncMessage("the player"+playerNickname+
                            "is too slow! His round has exceeded 2 minutes!"); /**todo: fare leggere il messaggio sul client**/


    /*for(PlayerManager playerManager:messageHandler.getPlayerManagerMap().values()){
                        playerManager.sendMessage(asyncMessage);
                    }


                    toStop = true;
                    if(pingThread.isInterrupted()==false){
                        pingThread.interrupt();
                    }

                    System.out.println("Player manager: the player"+playerNickname+ "is too slow! His round has exceeded 2 minutes!");
                    ex.printStackTrace();
                    ServerController.getInstance().closeConnection(playerNickname);

                    return;
                }

                //alla fine del timeout, tolgo il timeout. Il prossimo si riattiverà quando questa funzione
                //verrà chiamata nuovamente: cioè quando il gameController cercherà di leggere un messaggio dalla
                //coda di questo player manager
                try {
                    clientSocket.setSoTimeout(0);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*

    /*
    public void sendPingMessage(){


        JsonConverter jsonConverter= new JsonConverter();
        String stringToSend = jsonConverter.fromMessageToJson(new PingMessage());

        try {
            bufferedWriterOut.write(stringToSend);
            bufferedWriterOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

     */

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
}

package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.logic.ServerController;
import it.polimi.ingsw.server.model.Player;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.Future;

public class PlayerManager implements Runnable{
    private Queue<Message> messageQueue;
    private boolean characterReceived=false;
    private JsonConverter jsonConverter;
    private String playerNickname;
    private BufferedReader bufferedReaderIn;
    private BufferedWriter bufferedReaderOut;
    private boolean isMyTurn=false;
    private boolean toStop=false;

    public PlayerManager(String playerNickname, BufferedReader bufferedReaderIn, BufferedWriter bufferedReaderOut) {
        this.playerNickname = playerNickname;
        this.bufferedReaderIn=bufferedReaderIn;
        this.bufferedReaderOut=bufferedReaderOut;

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
    public void run(){
        String receivedString;
        Message receivedMessage;
        Message message;

        while (true) {
            receivedString = readFromBuffer();
            message = jsonConverter.fromJsonToMessage(receivedString);

            if(message.getMessageType()!=TypeOfMessage.Async){
                if(isMyTurn==false){
                    stringMessageToClient("It is not your turn");
                }
                else
                {
                    receivedMessage = (Message) jsonConverter.fromJsonToMessage(receivedString);

                    if(receivedMessage.getMessageType()==TypeOfMessage.CharacterCard){
                        characterReceived=true;
                    }

                    messageQueue.add(receivedMessage);
                }
            }
            //if i have received an async message(a disconnection message)
            else{
                System.out.println(message.getMessageType().toString());
                ServerController.getInstance().closeConnection(playerNickname);
            }
        }
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public void setCharacterReceived(boolean characterReceived) {
        this.characterReceived = characterReceived;
    }

    public boolean isCharacterReceived() {
        return characterReceived;
    }

    public Message getLastMessage() {
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
            ServerController.getInstance().closeConnection(playerNickname);
        }
        return lastMessage;
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
            ServerController.getInstance().closeConnection(playerNickname);
        }
    }

    public void sendMessage(Message message){
        String stringToSend = jsonConverter.fromMessageToJson(message);

        //null only in tests
        if (bufferedReaderOut == null){return;}

        try {
            bufferedReaderOut.write(stringToSend);
            bufferedReaderOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            ServerController.getInstance().closeConnection(playerNickname);
        }
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
    public GameMessage readMessage(TypeOfMessage typeOfMessage) {
        Message receivedMessage;
        GameMessage gameMessage;

        do {
            receivedMessage = getLastMessage();
            setTimeout();
            if (receivedMessage.getMessageType() != typeOfMessage) {
                Message errorGameMessage = new Message(TypeOfMessage.Error);
                String stringToSend = jsonConverter.fromMessageToJson(errorGameMessage);
                try {
                    bufferedReaderOut.write(stringToSend);
                    bufferedReaderOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    ServerController.getInstance().closeConnection(playerNickname);
                    this.toStop=true;
                    return null;
                }
            }
        } while (receivedMessage.getMessageType() != typeOfMessage);
        gameMessage = (GameMessage) receivedMessage;
        return gameMessage;
    }

    public boolean isToStop() {
        return toStop;

    }

    /**
     * stops the game if a player do not answer for more than 3 minutes
     * it is called in the readMessage method (in playerManager)
     * so it is called every thime the gameController asks to read the last message of the queue
     */
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
                    ex.printStackTrace();
                    DisconnectionMessage disconnectionMessage=new DisconnectionMessage("the player"+playerNickname+
                            "is too slow! His round has exceeded 2 minutes!");
                    for(PlayerManager playerManager:messageHandler.getPlayerManagerMap().values()){
                        playerManager.sendMessage(disconnectionMessage);
                    }
                    ServerController.getInstance().closeConnection(playerNickname);
                    return;
                }
            }
        }
    }
    public Queue<Message> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(Queue<Message> messageQueue) {
        this.messageQueue = messageQueue;
    }
}

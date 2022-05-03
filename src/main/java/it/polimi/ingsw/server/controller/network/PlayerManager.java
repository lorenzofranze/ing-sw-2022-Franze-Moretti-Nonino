package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.ServerController;

import java.io.*;
import java.util.Queue;

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
            if(receivedMessage.getMessageType()!=typeOfMessage){
                Message errorGameMessage=new Message(TypeOfMessage.Error);
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
}

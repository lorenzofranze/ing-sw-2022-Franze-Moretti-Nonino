package it.polimi.ingsw.client.CLI;

import java.io.IOException;
import java.util.Scanner;

public class PlayGamePianification {
    private final LineClient lineClient;

    public PlayGamePianification(LineClient lineClient){
        this.lineClient = lineClient;
    }
    String messageString;

    public void run() throws IOException{
        System.out.println("the game is starting");

    }

    /*
    public void run() throws IOException {


        messageString = lineClient.readFromBuffer(); //the game is starting - message
        System.out.println(messageString);
        System.out.flush();

        messageString =lineClient.readFromBuffer(); //pianification game - message
        System.out.println(messageString);
        System.out.flush();

        messageString= lineClient.readFromBuffer(); //game state message
        UpdateMessage gameStateMessage= (UpdateMessage) JsonConverter.fromJsonToMessage(messageString);

        //Non vado avanti fino a quando tutti i giocatori non hanno ricevuot il gamestate. Per coordinaremi, il server
        // deve ricevere un ACK da tutti i plyer
        ACKMessage gameStateCorrectlyReceived = new ACKMessage();
        gameStateCorrectlyReceived.setMessageType(TypeOfMessage.GameStateACK);
        String stringACKMessage = JsonConverter.fromMessageToJson(gameStateCorrectlyReceived);
        lineClient.getOut().write(stringACKMessage);
        lineClient.getOut().flush();

        //non vado avanto finchè non ricevo la notifica che tutti sono stati aggiornati
        messageString = lineClient.readFromBuffer(); //everyone updated - message   (non mi serve scriverlo in schermo)

        int num;
        boolean firstWhile=true;
        Message message;
        messageString=lineClient.readFromBuffer();
        message=JsonConverter.fromJsonToMessage(messageString);

        System.out.println("messaggio ricevuto di tipo" + message.getMessageType());
        while(message.getMessageType()==TypeOfMessage.GameState || message.getMessageType()==TypeOfMessage.AssistantCard) {
            if(message.getMessageType()==TypeOfMessage.AssistantCard) {
                if (!firstWhile) {
                    firstWhile = false;
                    System.out.println(lineClient.getIn().readLine()); //wrong choice - message
                    messageString = lineClient.readFromBuffer();
                    message = JsonConverter.fromJsonToMessage(messageString); //richiede scelta carta assistente
                }
                System.out.println("Scegli carta assistente");
                IntMessage assistantCardMessage = new IntMessage();
                assistantCardMessage.setMessageType(TypeOfMessage.AssistantCard);
                do {
                    System.out.println("inserisci carta assistente");
                    num = lineClient.readInt(scanner);
                } while (num < 1 || num > 10);
                assistantCardMessage.setValue(num);
                String stringAssistantCard = JsonConverter.fromMessageToJson(assistantCardMessage);
                lineClient.getOut().write(stringAssistantCard);
                lineClient.getOut().flush();
            }

            messageString=lineClient.readFromBuffer();
            message=JsonConverter.fromJsonToMessage(messageString);
            System.out.println("messaggio ricevuto di tipo" + message.getMessageType());

        }

        // ho già letto l'update - message


    }

     */

}

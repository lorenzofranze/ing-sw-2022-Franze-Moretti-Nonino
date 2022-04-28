package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Controller.Network.JsonConverter;
import it.polimi.ingsw.Server.Controller.Network.Messages.*;
import it.polimi.ingsw.Server.Model.AssistantCard;

import java.io.IOException;
import java.util.Scanner;

public class PlayGamePianification {
    private final LineClient lineClient;
    Scanner scanner = new Scanner(System.in);
    public PlayGamePianification(LineClient lineClient){
        this.lineClient = lineClient;
    }
    String messageString;

    public void run() throws IOException {

        messageString = lineClient.readFromBuffer(); //the game is starting - message
        System.out.println(messageString);
        System.out.flush();

        messageString =lineClient.readFromBuffer(); //pianification game - message
        System.out.println(messageString);
        System.out.flush();

        messageString= lineClient.readFromBuffer(); //game state message
        GameStateMessage gameStateMessage= (GameStateMessage) JsonConverter.fromJsonToMessage(messageString);

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
        while(message.getMessageType()!=TypeOfMessage.GameState) {
            if(!firstWhile){
                firstWhile=false;
                System.out.println(lineClient.getIn().readLine()); //wrong choice - message
                messageString=lineClient.readFromBuffer();
                message=JsonConverter.fromJsonToMessage(messageString); //richiede scelta carta assistente
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
            System.out.println("flag1");
            messageString=lineClient.readFromBuffer();
            System.out.println("flag2");
            message=JsonConverter.fromJsonToMessage(messageString);
            System.out.println("flag3");

        }

        // ho già letto l'update - message


    }

}

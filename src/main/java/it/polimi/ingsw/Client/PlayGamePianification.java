package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Controller.Network.JsonConverter;
import it.polimi.ingsw.Server.Controller.Network.Messages.GameStateMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.IntMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.Message;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Model.AssistantCard;

import java.io.IOException;
import java.util.Scanner;

public class PlayGamePianification implements Runnable{
    private final LineClient lineClient;
    Scanner scanner = new Scanner(System.in);
    public PlayGamePianification(LineClient lineClient){
        this.lineClient = lineClient;
    }

    String messageString;
    public void run() {
        try {
            messageString= lineClient.getIn().readLine(); //the game is starting - message
            System.out.println(messageString);
            lineClient.getIn().readLine(); //pianification game - message
            System.out.println(messageString);


            messageString= lineClient.getIn().readLine(); //game state message
            GameStateMessage gameStateMessage= (GameStateMessage) JsonConverter.fromJsonToMessage(messageString);

            int num;
            boolean firstWhile=true;
            Message message;
            messageString=lineClient.getIn().readLine();
            message=JsonConverter.fromJsonToMessage(messageString);
            while(message.getMessageType()!=TypeOfMessage.GameState) {
                if(!firstWhile){
                    firstWhile=false;
                    System.out.println(lineClient.getIn().readLine()); //worong choice - message
                    messageString=lineClient.getIn().readLine();
                    message=JsonConverter.fromJsonToMessage(messageString); //richiede scelta carta assistente
                }
                System.out.println("Scegli carta assistente");
                IntMessage assistantCardMessage = new IntMessage();
                assistantCardMessage.setMessageType(TypeOfMessage.AssistantCard);
                do {
                    System.out.println("inserisci carta assistente");
                    num = scanner.nextInt();
                } while (num < 1 || num > 10);
                assistantCardMessage.setValue(num);
                String stringAssistantCard = JsonConverter.fromMessageToJson(assistantCardMessage);
                lineClient.getOut().write(stringAssistantCard);
                lineClient.getOut().flush();
                messageString=lineClient.getIn().readLine();
                message=JsonConverter.fromJsonToMessage(messageString);
            }

            // ho già letto l'update - message

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

}

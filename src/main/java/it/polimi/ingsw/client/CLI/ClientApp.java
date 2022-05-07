package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.TypeOfMessage;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] Args) {
        //posso specificare con Args[0] ip e Args[1] porta per ora hardcodati

        LineClient lineClient = null;
        boolean resultConnection;
        Message response;

        do {
            resultConnection=false;
            Login login = new Login();
            lineClient = login.connect("localhost", 32501);
            resultConnection = (lineClient!=null) ? true : false;
        }while(resultConnection==false);

        System.out.println("BENVENUTO IN ERIANTYS la partita inizierà a breve");

        try {
            response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
        }catch(IOException ex){
            System.out.println("disconnection before starting game");
            return;
        }

        if(response.getMessageType()!= TypeOfMessage.ACK) {
            System.out.println("match closed");
            return;
        }

        ClientGameController clientGameController = new ClientGameController(lineClient);
        Thread match= new Thread(clientGameController);
        match.start();



        // qui faccio partire altri thread che verificano lo stato del gioco mentre match va avanti il player gioca

    }


}
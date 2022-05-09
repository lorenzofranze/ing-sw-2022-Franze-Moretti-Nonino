package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.common.messages.*;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] Args) {
        //posso specificare con Args[0] ip e Args[1] porta per ora hardcodati

        //connection to ip and port of the server
        LineClient lineClient = null;
        boolean resultConnection;
        Message response;
        do {
            resultConnection=false;
            Login login = new Login();
            lineClient = login.connect("localhost", 32501);
            resultConnection = (lineClient!=null) ? true : false;
        }while(resultConnection==false);
        System.out.println("WELCOME IN ERIANTIS! The game will start shortly.");

        try {
            response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
        }catch(IOException ex){
            System.out.println("disconnection before starting game");
            return;
        }

        //The client is waiting for the ACK mesage that is telling him the lobby is been completed

        if(response.getMessageType().equals(TypeOfMessage.Ack)) {
            AckMessage responseAck = (AckMessage) response;
            if (responseAck.getTypeOfAck().equals(TypeOfAck.CompleteLobby)){
                ClientGameController clientGameController = new ClientGameController(lineClient);
                Thread match= new Thread(clientGameController);
                match.start();
                // qui faccio partire altri thread che verificano lo stato del gioco mentre match va avanti il player gioca
            }
        }else{
            System.out.println("match closed");
            return;
        }


    }


}
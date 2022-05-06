package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;

import java.io.IOException;

public class Login {

    public LineClient connect(String ip, int port){
        LineClient lineClient;
        String nickname;
        int mod;
        boolean valid;
        Message response;
        System.out.println("modalità di gioco:\n" +
                "1. 2 players simple\n" +
                "2. 3 players simple\n" +
                "3. 2 players complex\n" +
                "4. 3 players complex");
        int resultConnection = -1;
        do {
            valid = true;
            mod = VIEWclientCLI.readInt();
            if (mod <= 0 || mod >= 5) {
                System.out.println("not valid");
                valid = false;
            }
        } while (!valid);


        ConnectionMessage cm = new ConnectionMessage(GameMode.values()[mod-1]);
        String stringToSend = JsonConverter.fromMessageToJson(cm);

        /* connect to the server */

        try {
            lineClient = new LineClient(ip, port);
            // TODO: da qui attivare timeout su server per ricevere messaggio entro poco tempo se no error message e chiusura connessione
        } catch (IOException e) {
            System.out.println("impossibile to connect");
            return null;
        }
        /*
        try {
            Thread.sleep(2000);
        }catch(InterruptedException ex){
            System.out.println("interrotto");
        }
         */

        /* send connection message*/
        try {
            lineClient.getOut().write(stringToSend);
            lineClient.getOut().flush();
        } catch (IOException e) {
            System.out.println("impossibile to send the message: disconnected");
            return null;
        }
        try {
            response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
        }catch(IOException ex){
            System.out.println("time out expired");
            return null;
        }
        if (response.getMessageType() == TypeOfMessage.Error) {
            System.out.println("connection refused");
            return null;
        }else if(response.getMessageType() == TypeOfMessage.ACK){
            System.out.println("the game will start soon...");
        }
        try {
            response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
        }catch(IOException ex){
            System.out.println("error before nickname choose");
        }

        //send nickname
        //TODO: attivare timeout sul server che chiude connessione se messaggio non arriva entro poco tempo
        // es client in 1 minuto deve scegliersi un nickname al volo
        do {
            valid = true;
            System.out.println("inserire nickname(almeno 4 caratteri):");
            nickname = VIEWclientCLI.readString();
            if (nickname == null || nickname.length() < 4)
                valid = false;

            NicknameMessage nm = new NicknameMessage(nickname);
            stringToSend = JsonConverter.fromMessageToJson(cm);

            try{
                lineClient.getOut().write(stringToSend);
                lineClient.getOut().flush();
            }catch(IOException ex){
                System.out.println("impossibile to send the message: disconnected");
                return null;
            }
            try {
                response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
            }catch(IOException ex){
                System.out.println("disconnection while choosing nickname");
            }
            if (response.getMessageType() == TypeOfMessage.Error) {
                System.out.println("nickname already in use");
                valid=false;
            }
        }while (!valid);

        return lineClient;
    }

}
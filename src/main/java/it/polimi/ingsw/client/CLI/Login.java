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
        Message response = null;
        String stringMessage;
        System.out.println("modalità di gioco:\n" +
                "1. 2 players simple\n" +
                "2. 3 players simple\n" +
                "3. 2 players complex\n" +
                "4. 3 players complex");

        do {
            valid = true;
            mod = VIEWclientCLI.readInt();
            if (mod <= 0 || mod >= 5) {
                System.out.println("not valid");
                valid = false;
            }
        } while (!valid);


        /* connect to the server */

        try {
            lineClient = new LineClient(ip, port);
        } catch (IOException e) {
            System.out.println("impossibile to connect");
            return null;
        }
        do {
            valid = true;
            System.out.println("inserire nickname(almeno 4 caratteri):");
            VIEWclientCLI.readString();
            nickname = VIEWclientCLI.readString();
            if (nickname == null || nickname.length() < 4)
                valid = false;
            if(valid){
                ConnectionMessage cm = new ConnectionMessage(nickname, GameMode.values()[mod-1] );
                stringMessage = JsonConverter.fromMessageToJson(cm);
                try{
                    lineClient.getOut().write(stringMessage);
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
                }else if(response.getMessageType() == TypeOfMessage.ACK)
                    System.out.println("the game will start soon...");
            }
        }while (!valid);

        return lineClient;
    }

}
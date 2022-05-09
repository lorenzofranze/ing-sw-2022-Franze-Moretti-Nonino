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
        Message receivedMessage = null;
        System.out.println("Insert game mode:\n" +
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
            System.out.println("insert nickname(at least 4 characters):");
            nickname = VIEWclientCLI.readString();
            if (nickname == null || nickname.length() < 4) {valid = false;}
            if(valid) {
                ConnectionMessage cm = new ConnectionMessage(nickname, GameMode.values()[mod - 1]);
                try {
                    lineClient.sendToServer(cm);
                } catch (IOException ex) {
                    System.out.println("impossibile to send the message: disconnected");
                    return null;
                }
                try {
                    receivedMessage = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                } catch (IOException ex) {
                    System.out.println("disconnection while choosing nickname");
                    return null;
                }

                if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)) {
                    AckMessage responseAck = (AckMessage) receivedMessage;
                    if (responseAck.getTypeOfAck().equals(TypeOfAck.CorrectConnection)) {
                        System.out.println("the choosen name is valid");
                        valid = true;
                        lineClient.setNickname(nickname);
                        return lineClient;
                    }
                }

                if (receivedMessage.getMessageType().equals(TypeOfMessage.Error)) {
                    ErrorMessage errorMessage = (ErrorMessage) receivedMessage;
                    if (errorMessage.getTypeOfError().equals(TypeOfError.UsedName)) {
                        System.out.println("name already in use");
                        valid = false;
                    }
                    if (errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice)) {
                        System.out.println("name not valid");
                        valid = false;
                    }
                }
            }
        }while (!valid);

        return lineClient;
    }

}
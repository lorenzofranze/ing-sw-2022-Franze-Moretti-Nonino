package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.common.messages.ConnectionMessage;

import java.io.IOException;
import java.util.Scanner;

import it.polimi.ingsw.common.messages.JsonConverter;

public class Login {
    private final LineClient lineClient;
    private Scanner scanner;

    public Login(LineClient lineClient){
        this.lineClient = lineClient;
        this.scanner = new Scanner(System.in);
    }

    public int start () throws IOException {

        String nickname;
        int mod;
        boolean valid;
        do {
            valid = true;
            System.out.println("inserire nickname(almeno 4 caratteri):");
            nickname = scanner.nextLine();
            if (nickname == null || nickname.length() < 4)
                valid = false;
        } while (!valid);

        lineClient.setNickname(nickname);

        System.out.println("modalità di gioco:\n" +
                "1. 2 players simple\n" +
                "2. 3 players simple\n" +
                "3. 2 players complex\n" +
                "4. 3 players complex");

        int resultConnection = -1;
        do {
            valid = true;
            mod = lineClient.readInt(scanner);
            if (mod <= 0 || mod >= 5) {
                System.out.println("not valid");
                valid = false;
            }
        } while (!valid);


        ConnectionMessage cm = new ConnectionMessage(nickname, GameMode.values()[mod-1]);
        String stringToSend = JsonConverter.fromMessageToJson(cm);
        try {
            lineClient.getOut().write(stringToSend);
            lineClient.getOut().flush();
        } catch (IOException e) {
            System.out.println("impossibile inviare il messaggio: disconnesso");
            throw e;
        }

        try {
            resultConnection = lineClient.getIn().read();
        }catch (IOException e){
            System.out.println("nessuna risposta dal server: disconnesso");
            throw  e;
        }

        return resultConnection;

    }

}
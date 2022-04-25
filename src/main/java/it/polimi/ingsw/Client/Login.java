package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Controller.GameMode;
import it.polimi.ingsw.Server.Controller.Network.Messages.ConnectionMessage;

import java.io.IOException;
import java.util.Scanner;

import it.polimi.ingsw.Server.Controller.Network.JsonConverter;

public class Login implements Runnable {
    private final LineClient lineClient;
    private int result=0;

    public Login(LineClient lineClient){
        this.lineClient = lineClient;
    }

    public void run (){
        Scanner scanner = new Scanner(System.in);
        String nickname;
        int mod;
        boolean valid;
        int result=0;
        do{
            valid = true;
            System.out.println("inserire nickname(almeno 4 caratteri):");
            nickname = scanner.nextLine();
            if(nickname== null || nickname.length()<4)
                valid = false;
        } while(!valid);

        System.out.println("modalità di gioco:\n" +
                "1. 2 players simple\n" +
                "2. 3 players simple\n" +
                "3. 2 players complex\n" +
                "4. 3 players complex");
        do{
            valid= true;
            mod = readInt(scanner);
            if(mod<=0 || mod >=5) {
                valid = false;
            }
        }while(!valid);

        ConnectionMessage cm = new ConnectionMessage(nickname, GameMode.values()[mod-1]);

        String stringToSend = JsonConverter.fromMessageToJson(cm);
        try {
            lineClient.getOut().write(stringToSend);
            System.out.println(stringToSend);
            lineClient.getOut().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("messaggio connessione inviato...");
        try {
            System.out.println("aspetto result");
            result = lineClient.getIn().read();
            System.out.println("result");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public int readInt(Scanner scanner){
        int val=0;
        boolean valid;
        do {
            valid=false;
            if(scanner.hasNextInt()) {
                val = scanner.nextInt();
                valid = true;
            }else{
                scanner.next();
                System.out.println("Input not valid");
            }
        }while(!valid);
        return val;
    }
}
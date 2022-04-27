package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Controller.GameMode;
import it.polimi.ingsw.Server.Controller.Network.Messages.ConnectionMessage;

import java.io.IOException;
import java.util.Scanner;

import it.polimi.ingsw.Server.Controller.Network.JsonConverter;

public class Login {
    private final LineClient lineClient;

    public Login(LineClient lineClient){
        this.lineClient = lineClient;
    }

    public int start () throws IOException {
        Scanner scanner = new Scanner(System.in);
        String nickname;
        int mod;
        boolean valid;
        int resultConnection = 0;
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
                System.out.println("not valid");
                valid = false;
            }
        }while(!valid);

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

package it.polimi.ingsw.Client;

import java.util.Scanner;

public class Login {
    private final Scanner scanner;

    public Login(Scanner scanner){
        this.scanner = scanner;
    }

    public void login(){
        String nickname;
        int mod;
        boolean valid;
        do{
            valid = true;
            System.out.println("inserire nickname(almeno 4 caratteri):");
            nickname = scanner.nextLine();
            if(nickname== null || nickname.length()<4)
                valid = false;
        } while(!valid);

        System.out.println("modalità di gioco: 1. 2 players simple\n" +
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

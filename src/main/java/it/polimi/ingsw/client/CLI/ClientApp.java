package it.polimi.ingsw.client.CLI;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {

        public static void main(String[] args) {
            int resultConnection;
            boolean gameOver=false;

            boolean connected=false;
            Scanner scanner;
            LineClient lineClient = null;
            int port=0;
            System.out.println("Scegli porta del server (consigliata: 32502)");
            scanner=new Scanner(System.in);

            while(!connected) {
                //readInt
                int val = 0;
                boolean valid;
                do {
                    valid = false;
                    if (scanner.hasNextInt()) {
                        port = scanner.nextInt();
                        valid = true;
                    } else {
                        scanner.next();
                        System.out.println("Input not valid");
                    }
                } while (!valid);

                try {
                    lineClient = new LineClient("localhost", port);
                    connected = true;
                } catch (IOException ex) {
                    System.out.println("impossibile connettersi provare con porta precedente + 1");
                }
            }


            do {
                Login login = new Login(lineClient);

                try {
                    resultConnection = login.start();
                } catch (IOException ex) {
                    return;
                }

                if(resultConnection==0)
                    System.out.println("nickname already in use");
                else if(resultConnection==1){
                    System.out.println("WELCOME IN ERIANTYS! The game will start soon");
                }


                else
                    System.out.println("general error: resultConnection = " + resultConnection);
            }while(resultConnection!=1);




            try {
                ClientGameController clientGameController = new ClientGameController(lineClient);
                clientGameController.play();

            }catch(IOException so){
                System.out.println("lost connection with the server");
                int reconnection = lineClient.reconnect(lineClient.getNickname());
                if(reconnection==0) return;
            }

        }


}

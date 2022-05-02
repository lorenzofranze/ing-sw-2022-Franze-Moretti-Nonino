package it.polimi.ingsw.client;

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

            /* client sceglie tra cli e gui */
            lineClient.chooseCLIorGUI();


            /* ora tutto nel try per proteggersi dalle disconnessioni
            * così se riceve un eccezione fa return e termina
             */
            try {
                PlayGamePianification playGamePianification = new PlayGamePianification(lineClient);
                playGamePianification.run();

            /* questo copre solo il caso in cui il server si spegne
            o questo giocatore si disconnette, da fare ancora caso in cui
            un altro giocatore contro cui sto giocando si disconnette -> pareggio a tutti
             */
            }catch(IOException so){
                System.out.println("lost connection with the server");
                int reconnection = lineClient.reconnect(lineClient.getNickname());
                if(reconnection==0) return;
            }

            try {
                PlayGameAction playGameAction = new PlayGameAction(lineClient);
                playGameAction.run();

            /* questo copre solo il caso in cui il server si spegne
            o questo giocatore si disconnette, da fare ancora caso in cui
            un altro giocatore contro cui sto giocando si disconnette -> pareggio a tutti
             */
            }catch(IOException so){
                System.out.println("lost connection with the server");
                int reconnection = lineClient.reconnect(lineClient.getNickname());
                if(reconnection==0) return;
            }
        }


}

package it.polimi.ingsw.Client;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

public class ClientApp {
        public static void main(String[] args) {
            int result;
            boolean gameOver=false;
            LineClient lineClient;

            do {
                try {
                    lineClient = new LineClient("localhost", 32501);
                } catch (IOException ex) {
                    System.out.println("impossibile connettersi");
                    return;
                }

                Login login = new Login(lineClient);

                try {
                    result = login.start();
                } catch (IOException ex) {
                    return;
                }

                if(result==-1)
                    System.out.println("nickname già in uso, riprovare");
                else if(result==1)
                    System.out.println("benvenuto, la partita inizierà a breve, attendendo altri giocatori...");
                else
                    System.out.println("errore generico");
            }while(result!=1);


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
                System.out.println("connessione con il server persa");
            }
        }
}

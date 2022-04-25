package it.polimi.ingsw.Client;

import java.util.Scanner;

public class ClientApp {
        public static void main(String[] args) {
            int result;
            boolean gameOver=false;

            //System.out.println("inserire ip server: ");
            Scanner scanner = new Scanner(System.in);
            LineClient lineClient = new LineClient("localhost", 32501);
            if(lineClient==null) {
                System.out.println("impossibile connettersi");
                return;
            }

            Login login = new Login(lineClient);
            login.run();
            PlayGamePianification playGamePianification =new PlayGamePianification(lineClient);
            playGamePianification.run();

        }
}

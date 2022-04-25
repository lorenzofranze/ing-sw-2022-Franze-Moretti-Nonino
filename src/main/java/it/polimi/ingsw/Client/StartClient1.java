package it.polimi.ingsw.Client;
import java.io.IOException;
import java.util.Scanner;

public class StartClient1 {
    public static void main(String Args[]){
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

        PlayGame playGame=new PlayGame(lineClient);
        try {
            playGame.startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

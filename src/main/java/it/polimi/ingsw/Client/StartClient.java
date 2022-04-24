package it.polimi.ingsw.Client;
import java.util.Scanner;

public class StartClient {
    public static void main(String Args[]){
        int result;

        //System.out.println("inserire ip server: ");
        Scanner scanner = new Scanner(System.in);


        do{
            LineClient lineClient = new LineClient("localhost", 32501);
            if(lineClient==null) {
                System.out.println("impossibile connettersi");
                return;
            }
            Login login = new Login(lineClient);
            result=login.login();
            if(result==-1)
                System.out.println("nickname già in uso, riprovare");
            else if(result==(1))
                System.out.println("benvenuto, la partita inizierà a breve, attendendo altri giocatori...");
            else
                System.out.println("errore generico");
        }while(result!=1);
    }
}

package it.polimi.ingsw.Client;
import java.util.Scanner;

public class StartClient {
    public static void main(String Args[]){
        System.out.println("inserire ip server: ");
        Scanner scanner = new Scanner(System.in); // da modificare es ip già noto
        LineClient lineClient = new LineClient(scanner.next(), 50000);

    }
}

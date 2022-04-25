package it.polimi.ingsw.Client;

import java.io.IOException;
import java.util.Scanner;

public class PlayGame implements Runnable{
    private final LineClient lineClient;
    Scanner scanner = new Scanner(System.in);
    public PlayGame(LineClient lineClient){
        this.lineClient = lineClient;
    }

    String message;
    public void run() {
        try {
            message= lineClient.getIn().readLine(); //the game is starting - message
            System.out.println(message);
            lineClient.getIn().readLine(); //pianification game - message
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

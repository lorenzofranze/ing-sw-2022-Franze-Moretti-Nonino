package it.polimi.ingsw.Client;

import java.io.IOException;
import java.util.Scanner;

public class PlayGame {
    private final LineClient lineClient;
    Scanner scanner = new Scanner(System.in);
    public PlayGame(LineClient lineClient){
        this.lineClient = lineClient;
    }

    public void startGame() throws IOException {
        lineClient.getIn().readLine(); //the game is starting - message
        lineClient.getIn().readLine(); //pianification game - message

    }

}

package it.polimi.ingsw;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class User {

    private boolean expert;
    private int numPlayers;

    public User() throws IOException{
        // Printing the read line
        System.out.println("Inserisci il nickname: ");

        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        // Reading data using readLine
        String nickname = reader.readLine();

        System.out.println("Vuoi inserire la modalità esperto? Y/n");

        String x = reader.readLine();

        if (x.equals("y")) expert = true;
        if (x.equals("n")) expert = false;

        System.out.println("Con quanti giocatori vuoi giocare? 2/3");

        String y = reader.readLine();

        numPlayers = Integer.parseInt(y);


    }

    public String getNickname() {
        return nickname;
    }

    private String nickname;

    public boolean isExpert() {
        return expert;
    }

    public int getNumPlayers() {
        return numPlayers;
    }


}

package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;
import java.util.Scanner;

public class MessageHandler {
    Scanner scanner = new Scanner(System.in);
    public int value;
    private int index =0;
    private int[] buffer = {1,2,3}; // inserire valori da provare con getValueTest()


    //restituisce il valore inserito dal giocatore player
    //in qualche modo chiedo il valore al giocatore e lo restituisco
    public int getValue(Player player) {
        return value;
    }

    /* chiede valore in generale a quel message handler,
     non sappiamo ancora se 1 per ogni player o cosa....
    legge valore da std input, non funziona con i test
     */
    public int getValue(){
        int a = scanner.nextInt();
        return a;
    }

    /* buffer di valori memorizzati, utile per test
    * ogni volta che leggi un valore passa a quello successivo */
    public int getValueTest(){
        index++;
        return buffer[index-1];
    }
}

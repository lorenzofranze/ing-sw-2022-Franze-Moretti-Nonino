package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Messages.ClientMessage;
import it.polimi.ingsw.Model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class MessageHandler {
    Scanner scanner = new Scanner(System.in);
    public int value;
    private int index =0;
    private int[] buffer = {1,2,3}; // inserire valori da provare con getValueTest()
    private ClientMessage lastMessage;
    private int portNumber;
    private Map<String,BufferedReader> bufferedReaderIn;
    private Map<String,BufferedReader> bufferedReaderOut;
    private ServerSocket serverSocket;


    //restituisce il valore inserito dal giocatore player
    //in qualche modo chiedo il valore al giocatore e lo restituisco
    public int getValue(Player player) {
        return value;
    }

    /* chiede valore in generale a quel message handler,
     non sappiamo ancora se 1 per ogni player o cosa....
    legge valore da std input, non funziona con i test
     */
    public int getValueCLI(String type, Player player){
        System.out.println(player.getNickname() +type);
        int a = scanner.nextInt();
        return a;
    }

    /* buffer di valori memorizzati, utile per test
    * ogni volta che leggi un valore passa a quello successivo */
    public int getValueTest(){
        index++;
        return buffer[index-1];
    }

    public void setWinner(Player winner){
        System.out.println(winner);
    }

    public void communication(GameController gameController) throws IOException {
        serverSocket = new ServerSocket(portNumber);

        for(Player player: gameController.getGame().getPlayers()){
            bufferedReaderIn.put( player.getNickname(),new BufferedReader(new InputStreamReader(new Socket().getInputStream())));
        }
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setLastMessage(ClientMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ClientMessage getLastMessage() {
        return lastMessage;
    }
}

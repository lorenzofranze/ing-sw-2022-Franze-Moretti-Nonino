package it.polimi.ingsw.Client;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LineClient {
    private String ip;
    private int port;
    private BufferedReader in;
    private BufferedWriter out;

    public LineClient(String ip, int port){
        this.ip = ip;
        this.port = port;

        Socket socket = null;

        try
        {
            socket = new Socket("localhost", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch(Exception e) { System.out.println(e.getMessage());}
    }

    public void startClient() throws IOException {

        String message;
        message = in.readLine();
        System.out.print("Messaggio Ricevuto : " + message);
    }

    public void endClient(){
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

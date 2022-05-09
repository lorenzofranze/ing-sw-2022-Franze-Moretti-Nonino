package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.ConnectionMessage;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.logic.GameMode;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class LineClient {
    private String ip;
    private int port;
    private String nickname;

    private BufferedReader in;
    private BufferedWriter out;
    private static int contatore = 0;


    public LineClient(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;

        Socket socket = null;
        socket = new Socket("localhost", port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println("connesso al server");

    }

    public void endClient(){
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void setOut(BufferedWriter out) {
        this.out = out;
    }

    /** read from network buffer */
    public String readFromBuffer() throws IOException{
        String lastMessage = "";

        try{
            String line = in.readLine();
            while (!("EOF").equals(line)){
                lastMessage = lastMessage + line + "\n";
                line = in.readLine();
            }
        } catch(IOException e){
            System.out.println("error in LineClient readFromBuffer");
            throw new IOException();
        }
        contatore++;
        System.out.println(("Messaggio letto numero: ") + contatore);
        System.out.println(lastMessage);

        return lastMessage;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }


    public int reconnect(String nickname, GameMode gameMode){
        ConnectionMessage cm = new ConnectionMessage( nickname, gameMode);
        String stringToSend = JsonConverter.fromMessageToJson(cm);
        try {
            this.getOut().write(stringToSend);
            this.getOut().flush();
        } catch (IOException e) {
            System.out.println("impossibile inviare il messaggio: disconnesso");
            return 0;
        }
        return 1;
    }

    public void sendToServer(Message message) throws IOException{
        String stringMessage = JsonConverter.fromMessageToJson(message);
        this.getOut().write(stringMessage);
        this.getOut().flush();
    }


}
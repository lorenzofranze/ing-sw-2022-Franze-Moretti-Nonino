package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Controller.GameMode;
import it.polimi.ingsw.Server.Controller.Network.JsonConverter;
import it.polimi.ingsw.Server.Controller.Network.Messages.ConnectionMessage;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LineClient {
    private String ip;
    private int port;
    private String nickname;
    private Scanner scanner;
    private boolean GUImode;

    private BufferedReader in;
    private BufferedWriter out;

    public LineClient(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;

        this.scanner = new Scanner(System.in);
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

    public String readFromBuffer(){
        String lastMessage = "";

        try{
            String line = in.readLine();
            while (!line.equals("EOF")){
                lastMessage = lastMessage + line + "\n";
                line = in.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        return lastMessage;
    }

    public int readInt(Scanner scanner){
        int val=0;
        boolean valid;
        do {
            valid=false;
            if(scanner.hasNextInt()) {
                val = scanner.nextInt();
                valid = true;
            }else{
                scanner.next();
                System.out.println("Input not valid");
            }
        }while(!valid);
        return val;
    }



    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public int reconnect(String nickname){
        ConnectionMessage cm = new ConnectionMessage(nickname, null);
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

    public void chooseCLIorGUI(){
        System.out.println("premi 1 per la cli, 2 per la gui");
        int result;
        do{
            result=readInt(scanner);
        }while(result!=1 && result!=2);
        if(result==1){
            GUImode=false;
        }
        else{
            GUImode=true;
        }

    }

    public Scanner getScanner() {
        return scanner;
    }
}

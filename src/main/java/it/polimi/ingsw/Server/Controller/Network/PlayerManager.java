package it.polimi.ingsw.Server.Controller.Network;

import it.polimi.ingsw.Server.Controller.Network.Messages.Message;
import it.polimi.ingsw.Server.Model.Player;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerManager implements Runnable{
    private Queue<Message> messageQueue;

    private String playerNickname;
    private BufferedReader bufferedReaderIn;
    private BufferedWriter bufferedReaderOut;

    public PlayerManager(String playerNickname, BufferedReader bufferedReaderIn, BufferedWriter bufferedReaderOut) {
        this.playerNickname = playerNickname;
        this.bufferedReaderIn=bufferedReaderIn;
        this.bufferedReaderOut=bufferedReaderOut;
    }

    public void run(){

    }
}

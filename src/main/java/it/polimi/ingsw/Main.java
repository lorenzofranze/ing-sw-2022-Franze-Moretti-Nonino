package it.polimi.ingsw;
import it.polimi.ingsw.Controller.ServerController;
import it.polimi.ingsw.Model.Character;
import java.io.*;

public class Main {
    public static void main (String Args[]) throws Exception {
        ServerController sc = ServerController.getInstance();
        sc.play();
    }



}

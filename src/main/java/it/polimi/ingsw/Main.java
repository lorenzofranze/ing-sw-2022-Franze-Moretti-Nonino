package it.polimi.ingsw;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Controller.GameMode;
import it.polimi.ingsw.Controller.Lobby;
import it.polimi.ingsw.Controller.ServerController;
import it.polimi.ingsw.Model.Character;
import it.polimi.ingsw.Model.Game;

import java.io.*;
import java.net.Socket;

public class Main {
    public static void main (String Args[]) throws Exception {
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());

        GameController gameController = new GameController(lobby, lobby.getGameMode().isExpert());
        gameController.run();
    }



}

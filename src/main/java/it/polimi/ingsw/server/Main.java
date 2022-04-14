package it.polimi.ingsw.server;
import it.polimi.ingsw.server.Controller.GameController;
import it.polimi.ingsw.server.Controller.GameMode;
import it.polimi.ingsw.server.Controller.Lobby;

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

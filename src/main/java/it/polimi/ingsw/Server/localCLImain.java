package it.polimi.ingsw.Server;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.GameMode;
import it.polimi.ingsw.Server.Controller.Network.Lobby;

import java.net.Socket;

public class localCLImain {
    public static void main (String Args[]) throws Exception {

        /*

        for(int i = 0; i < 1000; i++){
            Lobby lobby = new Lobby(GameMode.Simple_3);
            lobby.addUsersReadyToPlay("vale", new Socket());
            lobby.addUsersReadyToPlay("lara", new Socket());
            lobby.addUsersReadyToPlay("franzo", new Socket());

            GameController gameController = new GameController(lobby, lobby.getGameMode().isExpert());
            gameController.run();
        }

        for(int i = 0; i < 1000; i++){
            Lobby lobby = new Lobby(GameMode.Simple_2);
            lobby.addUsersReadyToPlay("vale", new Socket());
            lobby.addUsersReadyToPlay("lara", new Socket());

            GameController gameController = new GameController(lobby, lobby.getGameMode().isExpert());
            gameController.run();

        }


         */


        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());

        GameController gameController = new GameController(lobby, lobby.getGameMode().isExpert());
        gameController.run();
    }



}

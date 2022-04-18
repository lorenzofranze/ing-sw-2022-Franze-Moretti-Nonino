package it.polimi.ingsw;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.GameMode;
import it.polimi.ingsw.Server.Controller.Network.Lobby;
import it.polimi.ingsw.Server.Controller.SetUpPhase;
import it.polimi.ingsw.Server.Model.ColourTower;
import it.polimi.ingsw.Server.Model.ColourWizard;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SetUpPhaseTest {
    @Test
    public void testHandle() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());
        Game game = new Game(players, 3);

        GameController gameController = new GameController(lobby, false);
        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        setUpPhase.handle();

        for(Player p: gameController.getGame().getPlayers()){
            assertEquals(0, p.getCoins());
        }

    }

    public void testHandleComplexMode() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_2);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        Game game = new Game(players, 3);

        GameController gameController = new GameController(lobby, false);
        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        setUpPhase.handle();

        for(Player p: gameController.getGame().getPlayers()){
            assertEquals(1, p.getCoins());
        }

    }
}

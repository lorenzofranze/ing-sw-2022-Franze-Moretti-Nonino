package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GamePhaseTest {

    @Test
    public void testEquals() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, false);
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        GamePhase actionPhase = new ActionPhase(gameController);
        GamePhase pianificationPhase = new PianificationPhase(gameController);
        GamePhase setUpPhase = new SetUpPhase(gameController);

        assertEquals(true, actionPhase.equals(actionPhase));
        assertEquals(true, pianificationPhase.equals(pianificationPhase));
        assertEquals(true, setUpPhase.equals(setUpPhase));

        assertEquals(false, actionPhase.equals(null));

    }

}
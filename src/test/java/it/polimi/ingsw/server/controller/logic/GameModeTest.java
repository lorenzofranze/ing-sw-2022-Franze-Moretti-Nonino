package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameModeTest {

    @Test
    public void testGameMode(){
        GameMode gm = GameMode.Complex_3;
        assertEquals(3, gm.getNumPlayers());
        assertEquals(true, gm.isExpert());
    }

}
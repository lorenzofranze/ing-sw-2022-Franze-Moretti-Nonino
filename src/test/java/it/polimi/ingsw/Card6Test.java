package it.polimi.ingsw;

import it.polimi.ingsw.server.controller.characters.Card1;
import it.polimi.ingsw.server.controller.characters.Card6;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.*;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class Card6Test {

    public void card6Test(){
        Lobby lobby = new Lobby(GameMode.Complex_2);
        Socket socketVale = new Socket();
        Socket socketLara = new Socket();
        lobby.addUsersReadyToPlay("vale", socketVale);
        lobby.addUsersReadyToPlay("lara", socketLara);
        GameController gameController = new GameController(lobby, false);
        //ok ho creato il game ora vedo se l'effetto modifica
        Island island = gameController.getGame().getIslands().get(1);
        Player p1 =
        island.addStudents();


    }

}
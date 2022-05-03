package it.polimi.ingsw;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.logic.SetUpPhase;
import it.polimi.ingsw.server.controller.logic.SetUpResult;
import it.polimi.ingsw.server.model.*;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetUpPhaseTest {
    @Test
    public void testDistributeCoins() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());

        GameController gameController = new GameController(lobby, false);
        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        setUpPhase.handle();

        for(Player p: gameController.getGame().getPlayers()){
            assertEquals(0, p.getCoins());
        }

    }

    @Test
    public void testDistributeCoinsComplexModeCoins() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_2);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());

        GameController gameController = new GameController(lobby, true);
        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        setUpPhase.handle();

        for(Player p: gameController.getGame().getPlayers()){
            assertEquals(1, p.getCoins());
        }
    }

    @Test
    public void testPlacePawnsIslands(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_2);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());

        GameController gameController = new GameController(lobby, true);

        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        setUpPhase.handle();
        int numStudents;
        int motherNature=0;
        int motherNaturePosition=-1;

        for(Island island: gameController.getGame().getIslands()){
            if(island.getHasMotherNature()){
                motherNature++;
                motherNaturePosition=0;
                numStudents=island.getStudents().pawnsNumber();
                assertEquals(0, numStudents);
            }
            if(motherNaturePosition==6){
                numStudents=island.getStudents().pawnsNumber();
                assertEquals(0, numStudents);
            }
            if(motherNaturePosition!=6 && motherNaturePosition!=0){
                numStudents=island.getStudents().pawnsNumber();
                assertEquals(1, numStudents);
            }
            if(motherNaturePosition>=0){
                motherNaturePosition++;
            }
        }
        assertEquals(1, motherNature);
    }

    @Test
    public void testFillSchoolBoard2Players(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_2);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());

        GameController gameController = new GameController(lobby, true);

        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        setUpPhase.handle();

        for(Player p:gameController.getGame().getPlayers()){
            int numPawns=0;
            for (ColourPawn colourPawn:ColourPawn.values()){
                numPawns+=p.getSchoolBoard().getEntrance().get(colourPawn);
            }
            assertEquals(7,numPawns);
            assertEquals(8,p.getSchoolBoard().getSpareTowers());
        }
    }

    @Test
    public void testFillSchoolBoard3Players(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());

        GameController gameController = new GameController(lobby, true);

        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        setUpPhase.handle();

        for(Player p:gameController.getGame().getPlayers()) {
            int numPawns = 0;
            for (ColourPawn colourPawn : ColourPawn.values()) {
                numPawns += p.getSchoolBoard().getEntrance().get(colourPawn);
            }
            assertEquals(9, numPawns);
            assertEquals(6, p.getSchoolBoard().getSpareTowers());
        }
    }

    @Test
    public void testChooseFirstPianificationPlayer(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());

        GameController gameController = new GameController(lobby, true);

        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        SetUpResult ris=setUpPhase.handle();

        assertTrue(gameController.getGame().getPlayers().contains(ris.getFirstRandomPianificationPlayer()));
    }

}

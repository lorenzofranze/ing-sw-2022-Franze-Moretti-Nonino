package it.polimi.ingsw.server.controller.logic;

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
import java.util.List;

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

        int motherNaturePosition=-1;
        int count=0;
        List<Integer> empty = new ArrayList<>();

        for(Island island: gameController.getGame().getIslands()){
            if(island.getHasMotherNature()){
                motherNaturePosition=count;
                empty.add(count);
            }
            count++;
        }

        int numberOfIslands = gameController.getGame().getIslands().size();
        int oppositeMotherNature = (numberOfIslands/2+empty.get(0)) % numberOfIslands;

        empty.add(oppositeMotherNature);
        for(int i= 0; i< numberOfIslands; i++){
            if (empty.contains(i)){
                assertEquals(0, gameController.getGame().getIslands().get(i).getStudents().pawnsNumber());
            }
            else{
                assertEquals(1, gameController.getGame().getIslands().get(i).getStudents().pawnsNumber());
            }

        }
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

    @Test
    public void testToString(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());

        GameController gameController = new GameController(lobby, true);

        SetUpPhase setUpPhase = new SetUpPhase(gameController);
        String s = setUpPhase.toString();
        assertEquals(true, s.equals("SetUpPhase"));

    }

}

package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.UpdateMessage;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.*;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    public void testAddTwoMovements() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        GameController gameController = new GameController(lobby, true);
        ActionPhase actionPhase = new ActionPhase(gameController);
        gameController.setActionPhase(actionPhase);
        gameController.setCurrentPhase(actionPhase);
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        List<CharacterState> gameCharacters= new ArrayList<>();
        CharacterState c1 = new CharacterStateStudent(1, 1);
        CharacterState c2 = new CharacterStateNoEntryTile(5, 2);
        CharacterState c3 = new CharacterState(3, 4);
        gameCharacters.add(c1);
        gameCharacters.add(c2);
        gameCharacters.add(c3);
        g.setCharacterStates(gameCharacters);
        g.getIslands().get(0).setHasMotherNature(true);
        g.getIslands().get(1).setNumNoEntryTile(2);

        //results from pianificationPhase
        HashMap<Player, Integer> maximumMovements = new HashMap<>();

        maximumMovements.put(p1, 3);
        maximumMovements.put(p2, 2);
        maximumMovements.put(p3, 2);
        actionPhase.setMaximumMovements(maximumMovements);

        assertEquals(actionPhase, gameController.getCurrentPhase());

        assertEquals(2, actionPhase.getMaximumMovements().get(p2));

        gameController.addTwoMovements(p2);
        assertEquals(4, actionPhase.getMaximumMovements().get(p2));
    }

    @Test
    public void testCalculateWinner() {

        //--------------------------------------------------SOMEONE HAS FINISHED HIS TOWERS--------------------
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        GameController gameController = new GameController(lobby, true);
        PianificationResult pianificationResult = new PianificationResult();
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        List<CharacterState> gameCharacters= new ArrayList<>();
        CharacterState c1 = new CharacterStateStudent(1, 1);
        CharacterState c2 = new CharacterStateNoEntryTile(5, 2);
        CharacterState c3 = new CharacterState(3, 4);
        gameCharacters.add(c1);
        gameCharacters.add(c2);
        gameCharacters.add(c3);
        g.setCharacterStates(gameCharacters);
        g.getIslands().get(0).setHasMotherNature(true);

        p2.getSchoolBoard().setSpareTowers(0);

        gameController.calculateWinner();

        Player winner = gameController.getWinner();

        assertEquals(true, gameController.isGameOver());
        assertEquals(p2, winner);

        //--------------------------------------------------COUNTING WHO PLACED MORE TOWERS--------------------
        players = new ArrayList<>();
        lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        gameController = new GameController(lobby, true);
        pianificationResult = new PianificationResult();
        g = gameController.getGame();

        p1 = g.getPlayers().get(0);
        p2 = g.getPlayers().get(1);
        p3 = g.getPlayers().get(2);

        List<Island> islands = new ArrayList<>();
        Island island0 = new Island();
        island0.setTowerColor(p1.getColourTower());
        island0.addTower(3);
        Island island1 = new Island();
        island1.setTowerColor(p2.getColourTower());
        island1.addTower(4);
        Island island2 = new Island();
        Island island3 = new Island();
        island3.setTowerColor(p2.getColourTower());
        island3.addTower(3);
        islands.add(island0);
        islands.add(island1);
        islands.add(island2);
        islands.add(island3);
        g.setIslands(islands);

        gameCharacters= new ArrayList<>();
        c1 = new CharacterStateStudent(1, 1);
        c2 = new CharacterStateNoEntryTile(5, 2);
        c3 = new CharacterState(3, 4);
        gameCharacters.add(c1);
        gameCharacters.add(c2);
        gameCharacters.add(c3);
        g.setCharacterStates(gameCharacters);
        g.getIslands().get(0).setHasMotherNature(true);

        gameController.calculateWinner();

        assertEquals(true, gameController.isGameOver());
        assertEquals(p2, gameController.getWinner());

        //--------------------------------------------------TWO PEOPLE WITH SAME NUMBER OF PLACED TOWERS, BUT DIFFERENT PROFESSORS-----
        players = new ArrayList<>();
        lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        gameController = new GameController(lobby, true);
        pianificationResult = new PianificationResult();
        g = gameController.getGame();

        p1 = g.getPlayers().get(0);
        p2 = g.getPlayers().get(1);
        p3 = g.getPlayers().get(2);

        PawnsMap profP2 = new PawnsMap();
        profP2.add(ColourPawn.Yellow);
        profP2.add(ColourPawn.Pink);
        p2.getSchoolBoard().setProfessors(profP2);

        PawnsMap profP1 = new PawnsMap();
        profP1.add(ColourPawn.Green);
        profP1.add(ColourPawn.Red);
        profP1.add(ColourPawn.Blue);
        p1.getSchoolBoard().setProfessors(profP1);

        islands = new ArrayList<>();
        island0 = new Island();
        island0.setTowerColor(p1.getColourTower());
        island0.addTower(6);
        island1 = new Island();
        island1.setTowerColor(p2.getColourTower());
        island1.addTower(3);
        island2 = new Island();
        island3 = new Island();
        island3.setTowerColor(p2.getColourTower());
        island3.addTower(3);
        islands.add(island0);
        islands.add(island1);
        islands.add(island2);
        islands.add(island3);
        g.setIslands(islands);

        gameCharacters= new ArrayList<>();
        c1 = new CharacterStateStudent(1, 1);
        c2 = new CharacterStateNoEntryTile(5, 2);
        c3 = new CharacterState(3, 4);
        gameCharacters.add(c1);
        gameCharacters.add(c2);
        gameCharacters.add(c3);
        g.setCharacterStates(gameCharacters);
        g.getIslands().get(0).setHasMotherNature(true);


        gameController.calculateWinner();

        assertEquals(true, gameController.isGameOver());
        assertEquals(p1, gameController.getWinner());

        //--------------------------------------------------TWO PEOPLE WITH SAME NUMBER OF PLACED TOWERS AND PROFESSORS-----
        players = new ArrayList<>();
        lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        gameController = new GameController(lobby, true);
        pianificationResult = new PianificationResult();
        g = gameController.getGame();

        p1 = g.getPlayers().get(0);
        p2 = g.getPlayers().get(1);
        p3 = g.getPlayers().get(2);

        profP2 = new PawnsMap();
        profP2.add(ColourPawn.Yellow);
        profP2.add(ColourPawn.Pink);
        p2.getSchoolBoard().setProfessors(profP2);

        profP1 = new PawnsMap();
        profP1.add(ColourPawn.Green);
        profP1.add(ColourPawn.Red);
        p1.getSchoolBoard().setProfessors(profP1);

        islands = new ArrayList<>();
        island0 = new Island();
        island0.setTowerColor(p1.getColourTower());
        island0.addTower(6);
        island1 = new Island();
        island1.setTowerColor(p2.getColourTower());
        island1.addTower(3);
        island2 = new Island();
        island3 = new Island();
        island3.setTowerColor(p2.getColourTower());
        island3.addTower(3);
        islands.add(island0);
        islands.add(island1);
        islands.add(island2);
        islands.add(island3);
        g.setIslands(islands);

        gameCharacters= new ArrayList<>();
        c1 = new CharacterStateStudent(1, 1);
        c2 = new CharacterStateNoEntryTile(5, 2);
        c3 = new CharacterState(3, 4);
        gameCharacters.add(c1);
        gameCharacters.add(c2);
        gameCharacters.add(c3);
        g.setCharacterStates(gameCharacters);
        g.getIslands().get(0).setHasMotherNature(true);

        gameController.calculateWinner();

        GameStatePojo gameStatePojo = gameController.getGameState();
        assertEquals("?", gameStatePojo.getWinner());

        assertEquals(true, gameController.isGameOver());
        assertEquals("?", gameController.getWinner().getNickname());
    }

    @Test
    public void testUpdateSinglePlayer(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        GameController gameController = new GameController(lobby, true);
        PianificationResult pianificationResult = new PianificationResult();
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        List<CharacterState> gameCharacters= new ArrayList<>();
        CharacterState c1 = new CharacterStateStudent(1, 1);
        CharacterState c2 = new CharacterStateNoEntryTile(5, 2);
        CharacterState c3 = new CharacterState(3, 4);
        gameCharacters.add(c1);
        gameCharacters.add(c2);
        gameCharacters.add(c3);
        g.setCharacterStates(gameCharacters);
        g.getIslands().get(0).setHasMotherNature(true);

        gameController.updateSinglePlayer("vale");
    }

    @Test
    public void testSetCurrentPlayer(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        GameController gameController = new GameController(lobby, false);
        PianificationResult pianificationResult = new PianificationResult();
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        gameController.setCurrentPlayer(p2);
        assertEquals("lara", gameController.getCurrentPlayer().getNickname());
    }

    @Test
    public void testSetGameOver(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        GameController gameController = new GameController(lobby, false);
        PianificationResult pianificationResult = new PianificationResult();
        Game g = gameController.getGame();
        gameController.setGameOver(true);
        assertEquals(true, gameController.isGameOver());
    }

    @Test
    public void testGetActionPhase(){
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        GameController gameController = new GameController(lobby, false);
        assertEquals(lobby, gameController.getLobby());
        PianificationResult pianificationResult = new PianificationResult();
        Game g = gameController.getGame();
        gameController.setGameOver(true);
        ActionPhase actionPhase = new ActionPhase(gameController);
        gameController.setActionPhase(actionPhase);
        assertEquals(actionPhase, gameController.getActionPhase());
        gameController.setForceStop(true);
        assertEquals(true, gameController.isForceStop());
    }


}
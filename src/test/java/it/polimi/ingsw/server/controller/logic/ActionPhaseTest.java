package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.GameMessage;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.PawnsMap;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ActionPhaseTest {

    @Test
    public void testMoveSingleStudent() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());
        GameController gameController = new GameController(lobby, false);
        ActionPhase a = new ActionPhase(gameController);
        Game g = gameController.getGame();

        gameController.setCurrentPlayer(gameController.getGame().getPlayers().get(0));
        gameController.getCurrentPlayer().getSchoolBoard().getEntrance().add(ColourPawn.Yellow);
        int preD = gameController.getGame().getPlayers().get(0).getSchoolBoard().getDiningRoom().get(ColourPawn.Yellow);
        int preE = gameController.getGame().getPlayers().get(0).getSchoolBoard().getEntrance().get(ColourPawn.Yellow);
        a.moveSingleStudent(ColourPawn.Yellow, -1);
        int postD = gameController.getGame().getPlayers().get(0).getSchoolBoard().getDiningRoom().get(ColourPawn.Yellow);
        int postE = gameController.getGame().getPlayers().get(0).getSchoolBoard().getEntrance().get(ColourPawn.Yellow);
        assertEquals(1, postD-preD);
        assertEquals(1, preE-postE);

        gameController.setCurrentPlayer(gameController.getGame().getPlayers().get(2));
        gameController.getCurrentPlayer().getSchoolBoard().getEntrance().add(ColourPawn.Blue);
        int preI = gameController.getGame().getIslands().get(4).getStudents().get(ColourPawn.Blue);
        preE = gameController.getCurrentPlayer().getSchoolBoard().getEntrance().get(ColourPawn.Blue);
        a.moveSingleStudent(ColourPawn.Blue, 4);
        int postI = gameController.getGame().getIslands().get(4).getStudents().get(ColourPawn.Blue);
        postE = gameController.getCurrentPlayer().getSchoolBoard().getEntrance().get(ColourPawn.Blue);
        assertEquals(1, preE-postE);
        assertEquals(1, postI-preI);
    }

    @Test
    public void testMoveStudent() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());
        GameController gameController = new GameController(lobby, false);
        ActionPhase actionPhase = new ActionPhase(gameController);
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        HashMap<Player, Integer> maximumMovements = new HashMap<>();
        List<Player> turnOrder = new ArrayList<>();

        //results from pianificationPhase
        turnOrder.add(p2);
        turnOrder.add(p3);
        turnOrder.add(p1);
        actionPhase.setTurnOrder(turnOrder);

        maximumMovements.put(p1, 3);
        maximumMovements.put(p2, 2);
        maximumMovements.put(p3, 2);
        actionPhase.setMaximumMovements(maximumMovements);

        //initialize entances of Schoolboards
        PawnsMap entrance = new PawnsMap();
        entrance.add(ColourPawn.Blue, 2);
        entrance.add(ColourPawn.Yellow, 3);
        entrance.add(ColourPawn.Pink, 2);
        p1.getSchoolBoard().setEntrance(entrance);

        entrance = new PawnsMap();
        entrance.add(ColourPawn.Blue, 3);
        entrance.add(ColourPawn.Green, 3);
        entrance.add(ColourPawn.Pink, 1);
        p2.getSchoolBoard().setEntrance(entrance);

        entrance = new PawnsMap();
        entrance.add(ColourPawn.Blue, 2);
        entrance.add(ColourPawn.Pink, 3);
        entrance.add(ColourPawn.Yellow, 2);
        p3.getSchoolBoard().setEntrance(entrance);

        actionPhase.setCurrPlayer(p2);

        Map<String, PlayerManager> playerManagerMap = new HashMap<>();

        PlayerManager pm1 = new PlayerManager(p1.getNickname());
        PlayerManager pm2 = new PlayerManager(p2.getNickname());
        PlayerManager pm3 = new PlayerManager(p3.getNickname());
        Queue<Message> q1 = new ArrayDeque<>();
        Queue<Message> q2 = new ArrayDeque<>();
        Queue<Message> q3 = new ArrayDeque<>();

        //p1 moves Blue student to DiningRoom
        Message m1 = new GameMessage(TypeOfMessage.StudentColour, 1);
        q1.add(m1);
        m1 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q1.add(m1);
        //p1 moves Blue student to Island1
        m1 = new GameMessage(TypeOfMessage.StudentColour, 1);
        q1.add(m1);
        m1 = new GameMessage(TypeOfMessage.StudentMovement, 1);
        q1.add(m1);
        //p1 moves Yellow student to Island3
        m1 = new GameMessage(TypeOfMessage.StudentColour, 0);
        q1.add(m1);
        m1 = new GameMessage(TypeOfMessage.StudentMovement, 3);
        q1.add(m1);
        //p1 moves incorrectly
        m1 = new GameMessage(TypeOfMessage.StudentColour, 7);
        q1.add(m1);
        //p1 moves incorrectly
        m1 = new GameMessage(TypeOfMessage.StudentColour, 3);
        q1.add(m1);
        //p1 moves Pink student to DiningRoom
        m1 = new GameMessage(TypeOfMessage.StudentColour, 4);
        q1.add(m1);
        m1 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q1.add(m1);

        p2.getSchoolBoard().getDiningRoom().add(ColourPawn.Blue, 9);
        PawnsMap diningP2Pre = p2.getSchoolBoard().getDiningRoom().clone();

        //p2 moves Blue student to DiningRoom
        Message m2 = new GameMessage(TypeOfMessage.StudentColour, 1);
        q2.add(m2);
        m2 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q2.add(m2);
        //p2 moves Blue student to DiningRoom, but it is full
        m2 = new GameMessage(TypeOfMessage.StudentColour, 1);
        q2.add(m2);
        m2 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q2.add(m2);
        //p2 moves Blue student to Island4
        m2 = new GameMessage(TypeOfMessage.StudentColour, 1);
        q2.add(m2);
        m2 = new GameMessage(TypeOfMessage.StudentMovement, 4);
        q2.add(m2);
        //p2 moves Green student to Island7
        m2 = new GameMessage(TypeOfMessage.StudentColour, 2);
        q2.add(m2);
        m2 = new GameMessage(TypeOfMessage.StudentMovement, 7);
        q2.add(m2);
        //p2 moves incorrectly
        m2 = new GameMessage(TypeOfMessage.StudentColour, 7);
        q2.add(m2);
        //p2 moves incorrectly
        m2 = new GameMessage(TypeOfMessage.StudentColour, 0);
        q2.add(m2);
        //p2 moves incorrectly
        m2 = new GameMessage(TypeOfMessage.StudentColour, 2);
        q2.add(m2);
        m2 = new GameMessage(TypeOfMessage.StudentMovement, -5);
        q2.add(m2);
        //p2 moves Green student to DiningRoom
        m2 = new GameMessage(TypeOfMessage.StudentColour, 2);
        q2.add(m2);
        m2 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q2.add(m2);

        //p3 moves Yellow student to DiningRoom
        Message m3 = new GameMessage(TypeOfMessage.StudentColour, 0);
        q3.add(m3);
        m3 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q3.add(m3);
        //p3 moves Yellow student to DiningRoom
        m3 = new GameMessage(TypeOfMessage.StudentColour, 0);
        q3.add(m3);
        m3 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q3.add(m3);
        //p3 moves Pink student to DiningRoom
        m3 = new GameMessage(TypeOfMessage.StudentColour, 4);
        q3.add(m3);
        m3 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q3.add(m3);
        //p3 moves Pink student to DiningRoom
        m3 = new GameMessage(TypeOfMessage.StudentColour, 4);
        q3.add(m3);
        m3 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q3.add(m3);
        //p3 moves Pink student to DiningRoom
        m3 = new GameMessage(TypeOfMessage.StudentColour, 4);
        q3.add(m3);
        m3 = new GameMessage(TypeOfMessage.StudentMovement, -1);
        q3.add(m3);
        //p3 moves Blue student to Island10
        m3 = new GameMessage(TypeOfMessage.StudentColour, 1);
        q3.add(m3);
        m3 = new GameMessage(TypeOfMessage.StudentMovement, 10);
        q3.add(m3);

        pm1.setMessageQueue(q1);
        pm2.setMessageQueue(q2);
        pm3.setMessageQueue(q3);
        playerManagerMap.put(p1.getNickname(), pm1);
        playerManagerMap.put(p2.getNickname(), pm2);
        playerManagerMap.put(p3.getNickname(), pm3);

        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        actionPhase.moveStudent();

        PawnsMap entranceP2Post = new PawnsMap();
        entranceP2Post.add(ColourPawn.Blue, 1);
        entranceP2Post.add(ColourPawn.Green, 1);
        entranceP2Post.add(ColourPawn.Pink, 1);
        assertEquals(entranceP2Post, p2.getSchoolBoard().getEntrance());

        PawnsMap diningP2Post = diningP2Pre.clone();
        diningP2Post.add(ColourPawn.Blue, 1);
        diningP2Post.add(ColourPawn.Green, 1);

        assertEquals(diningP2Post, p2.getSchoolBoard().getDiningRoom());
    }

    @Test
    public void testMoveMotherNature() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());
        GameController gameController = new GameController(lobby, false);
        ActionPhase actionPhase = new ActionPhase(gameController);
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        HashMap<Player, Integer> maximumMovements = new HashMap<>();
        List<Player> turnOrder = new ArrayList<>();

        //results from pianificationPhase
        maximumMovements.put(p1, 3);
        maximumMovements.put(p2, 2);
        maximumMovements.put(p3, 2);
        actionPhase.setMaximumMovements(maximumMovements);

        gameController.setCurrentPlayer(p2);

        Map<String, PlayerManager> playerManagerMap = new HashMap<>();

        PlayerManager pm1 = new PlayerManager(p1.getNickname());
        PlayerManager pm2 = new PlayerManager(p2.getNickname());
        PlayerManager pm3 = new PlayerManager(p3.getNickname());
        Queue<Message> q1 = new ArrayDeque<>();
        Queue<Message> q2 = new ArrayDeque<>();
        Queue<Message> q3 = new ArrayDeque<>();

        Message m1 = new GameMessage(TypeOfMessage.StudentColour, 1);
        q1.add(m1);

        //incorrect Move mothernature
        Message m2 = new GameMessage(TypeOfMessage.MoveMotherNature, 0);
        q2.add(m2);
        //incorrect Move mothernature
        m2 = new GameMessage(TypeOfMessage.MoveMotherNature, 3);
        q2.add(m2);
        //correct Move mothernature
        m2 = new GameMessage(TypeOfMessage.MoveMotherNature, 1);
        q2.add(m2);

        //p3 moves Yellow student to DiningRoom
        Message m3 = new GameMessage(TypeOfMessage.StudentColour, 0);
        q3.add(m3);

        pm1.setMessageQueue(q1);
        pm2.setMessageQueue(q2);
        pm3.setMessageQueue(q3);
        playerManagerMap.put(p1.getNickname(), pm1);
        playerManagerMap.put(p2.getNickname(), pm2);
        playerManagerMap.put(p3.getNickname(), pm3);

        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        g.getIslands().get(3).setHasMotherNature(true);

        actionPhase.moveMotherNature(p2);

        assertEquals(false, g.getIslands().get(3).getHasMotherNature());
        assertEquals(true, g.getIslands().get(4).getHasMotherNature());
    }

    @Test
    public void testCheckEnd() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());
        GameController gameController = new GameController(lobby, false);
        ActionPhase actionPhase = new ActionPhase(gameController);
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        actionPhase.getActionResult().setFinishedTowers(true);
        assertEquals(true, actionPhase.checkEnd());

        actionPhase.getActionResult().setFinishedTowers(false);
        actionPhase.getActionResult().setThreeOrLessIslands(true);
        assertEquals(true, actionPhase.checkEnd());

        actionPhase.getActionResult().setFinishedTowers(false);
        actionPhase.getActionResult().setThreeOrLessIslands(false);
        assertEquals(false, actionPhase.checkEnd());
    }

    @Test
    public void testToString() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket());
        lobby.addUsersReadyToPlay("lara", new Socket());
        lobby.addUsersReadyToPlay("franzo", new Socket());
        GameController gameController = new GameController(lobby, false);
        ActionPhase actionPhase = new ActionPhase(gameController);
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        String s = actionPhase.toString();
        assertEquals("ActionPhase", s);
    }


}
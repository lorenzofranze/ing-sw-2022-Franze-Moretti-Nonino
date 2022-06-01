package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.common.messages.GameMessage;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.common.messages.TypeOfMove;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class PianificationPhaseTest {

    @Test
    public void testToString() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));


        GameController gameController = new GameController(lobby, false);
        PianificationPhase p = new PianificationPhase(gameController);

        String s = p.toString();
        assertEquals(true, s.equals("PianificationPhase"));
    }

    @Test
    public void testCheckPermit() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));


        GameController gameController = new GameController(lobby, false);
        PianificationPhase p = new PianificationPhase(gameController);
        Game g = gameController.getGame();
        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        p1.playAssistantCard(1);
        p2.playAssistantCard(2);

        Set<AssistantCard> deck = p3.getDeck();
        AssistantCard played = null;
        for(AssistantCard a: deck){
            if (a.getTurnOrder() == 1){
                played = a;
            }
        }
        p3.playAssistantCard(1);
        assertEquals(false, p.checkPermit(p3, played));

        players = new ArrayList<>();
        lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));

        gameController = new GameController(lobby, false);
        p = new PianificationPhase(gameController);
        g = gameController.getGame();
        p1 = g.getPlayers().get(0);
        p2 = g.getPlayers().get(1);
        p3 = g.getPlayers().get(2);

        p1.playAssistantCard(1);
        p2.playAssistantCard(2);
        for (int i = 2; i<11; i++){
            p3.playAssistantCard(i);
        }

        deck = p3.getDeck();
        played = null;
        for(AssistantCard a: deck){
            if (a.getTurnOrder() == 1){
                played = a;
            }
        }
        p3.playAssistantCard(1);
        assertEquals(true, p.checkPermit(p3, played));
    }


    @Test
    public void testFillCloud() {

        //3 players - cannot fill all clouds
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));


        GameController gameController = new GameController(lobby, false);
        PianificationPhase p = new PianificationPhase(gameController);
        Game g = gameController.getGame();
        g.getStudentsBag().removeRandomly(120);

        p.fillClouds();

        for(Cloud c :g.getClouds()){
            assertEquals(true, c.getStudents().pawnsNumber()==4 || c.getStudents().pawnsNumber()==0);
        }
        assertEquals(true, p.isFinishedStudentBag());

        //2 players - cannot fill all clouds
        players = new ArrayList<>();
        lobby = new Lobby(GameMode.Simple_2);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));

        gameController = new GameController(lobby, false);
        p = new PianificationPhase(gameController);
        g = gameController.getGame();
        g.getStudentsBag().removeRandomly(127);

        p.fillClouds();
        g = gameController.getGame();

        for(Cloud c :g.getClouds()){
            assertEquals(true, c.getStudents().pawnsNumber()==3 || c.getStudents().pawnsNumber()==0);
        }
        assertEquals(true, p.isFinishedStudentBag());


        //3 players - can fill all clouds
        players = new ArrayList<>();
        lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));


        gameController = new GameController(lobby, false);
        p = new PianificationPhase(gameController);
        g = gameController.getGame();

        p.fillClouds();
        g = gameController.getGame();

        for(Cloud c :g.getClouds()){
            assertEquals(true, c.getStudents().pawnsNumber()==4);
        }
        assertEquals(false, p.isFinishedStudentBag());

        //2 players - can fill all clouds
        players = new ArrayList<>();
        lobby = new Lobby(GameMode.Simple_2);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));

        gameController = new GameController(lobby, false);
        p = new PianificationPhase(gameController);
        g = gameController.getGame();

        p.fillClouds();
        g = gameController.getGame();

        for(Cloud c :g.getClouds()){
            assertEquals(true, c.getStudents().pawnsNumber()==3);
        }
        assertEquals(false, p.isFinishedStudentBag());
    }

    @Test
    public void testPlayAssistantCard() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));


        GameController gameController = new GameController(lobby, false);
        PianificationPhase p = new PianificationPhase(gameController);
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        HashMap turnOrderMap = new HashMap<Player, Integer>();
        HashMap maximumMovements = new HashMap<Player, Integer>();

        gameController.setCurrentPlayer(p1);
        Map<String, PlayerManager> playerManagerMap = new HashMap<>();

        PlayerManager pm1 = new PlayerManager(null, null);
        PlayerManager pm2 = new PlayerManager(null, null);
        PlayerManager pm3 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q1 = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Message> q2= new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Message> q3 = new LinkedBlockingQueue<>();
        Message m1 = new GameMessage(TypeOfMove.AssistantCard, 20);
        q1.add(m1);
        m1 = new GameMessage(TypeOfMove.AssistantCard, 3);
        q1.add(m1);
        m1 = new GameMessage(TypeOfMove.AssistantCard, 6);
        q1.add(m1);
        Message m2 = new GameMessage(TypeOfMove.AssistantCard, 3);
        q2.add(m2);
        Message m3 = new GameMessage(TypeOfMove.AssistantCard, 3);
        q3.add(m3);

        AssistantCard a = new AssistantCard(3, 2);
        AssistantCard b = new AssistantCard(4, 2);
        AssistantCard c = new AssistantCard(6, 3);
        p2.setPlayedAssistantCard(a);
        p3.setPlayedAssistantCard(b);

        pm1.setMessageQueue(q1);
        pm2.setMessageQueue(q2);
        pm3.setMessageQueue(q3);

        playerManagerMap.put(p1.getNickname(), pm1);
        playerManagerMap.put(p2.getNickname(), pm2);
        playerManagerMap.put(p3.getNickname(), pm3);

        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        p.playAssistantCard(p1, turnOrderMap, maximumMovements);
        assertEquals(true, p1.getPlayedAssistantCard().equals(c));

    }

    @Test
    public void testHandle() {
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Simple_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));


        GameController gameController = new GameController(lobby, false);
        PianificationPhase p = new PianificationPhase(gameController);
        Game g = gameController.getGame();

        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        Player p3 = g.getPlayers().get(2);

        gameController.setCurrentPlayer(p1);
        Map<String, PlayerManager> playerManagerMap = new HashMap<>();

        PlayerManager pm1 = new PlayerManager(null, null);
        PlayerManager pm2 = new PlayerManager(null, null);
        PlayerManager pm3 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q1 = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Message> q2= new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Message> q3 = new LinkedBlockingQueue<>();

        Message m1 = new GameMessage(TypeOfMove.AssistantCard, 6);
        q1.add(m1);
        Message m2 = new GameMessage(TypeOfMove.AssistantCard, 3);
        q2.add(m2);
        Message m3 = new GameMessage(TypeOfMove.AssistantCard, 4);
        q3.add(m3);

        AssistantCard c = new AssistantCard(6, 3);
        AssistantCard a = new AssistantCard(3, 2);
        AssistantCard b = new AssistantCard(4, 2);


        pm1.setMessageQueue(q1);
        pm2.setMessageQueue(q2);
        pm3.setMessageQueue(q3);
        playerManagerMap.put(p1.getNickname(), pm1);
        playerManagerMap.put(p2.getNickname(), pm2);
        playerManagerMap.put(p3.getNickname(), pm3);

        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        PianificationResult result = p.handle(p1);
        assertEquals(true, p1.getPlayedAssistantCard().equals(c));
        assertEquals(true, p2.getPlayedAssistantCard().equals(a));
        assertEquals(true, p3.getPlayedAssistantCard().equals(b));

        assertEquals(false, result.isFinishedAssistantCard());
        assertEquals(false, result.isFinishedStudentBag());

        Map maxMov = result.getMaximumMovements();
        List turnList = result.getTurnOrder();

        assertEquals(true, turnList.get(0).equals(p2));
        assertEquals(true, turnList.get(1).equals(p3));
        assertEquals(true, turnList.get(2).equals(p1));
        assertEquals(3, maxMov.get(p1));

    }
}
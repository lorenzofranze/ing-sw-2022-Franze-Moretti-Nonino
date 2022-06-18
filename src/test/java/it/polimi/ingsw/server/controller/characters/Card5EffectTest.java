package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.messages.GameMessage;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.PawnMovementMessage;
import it.polimi.ingsw.common.messages.TypeOfMove;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.logic.GameMode;
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

class Card5EffectTest {

    @Test
    public void testCard5Effect(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);


        //creation of Card5Effect
        CharacterStateNoEntryTile c5 = new CharacterStateNoEntryTile(5, 2);
        Card5Effect card5Effect = new Card5Effect(gameController, c5);

        assertEquals(4, c5.getNumNoEntry());
    }

    @Test
    public void testDoEffect(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);


        //creation of Card5Effect
        CharacterStateNoEntryTile c5 = new CharacterStateNoEntryTile(5, 2);
        Card5Effect card5Effect = new Card5Effect(gameController, c5);

        //creation of other two Characters and insertion of charaters in game
        CharacterState c2 = new CharacterState(2, 2);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c5);
        gameCharacters.add(c2);
        gameCharacters.add(c3);
        gameController.getGame().setCharacterStates(gameCharacters);

        //creation of players
        Player p1 = gameController.getGame().getPlayers().get(0);
        Player p2 = gameController.getGame().getPlayers().get(1);
        Player p3 = gameController.getGame().getPlayers().get(2);
        p1.addCoins(3);
        p2.addCoins(5);
        p3.addCoins(0);

        //p1 is the current player
        gameController.setCurrentPlayer(p1);

        //creation of playerManagerMap
        Map<String, PlayerManager> playerManagerMap = new HashMap<>();
        PlayerManager pm1 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q1= new LinkedBlockingQueue<>();
        PlayerManager pm2 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q2= new LinkedBlockingQueue<>();
        PlayerManager pm3 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q3= new LinkedBlockingQueue<>();

        //DIFFERENT CASES

        //1) the island is invalid
        Message m1 = new GameMessage(TypeOfMove.IslandChoice, 13);
        q1.add(m1);
        //2) valid movement
        m1 = new GameMessage(TypeOfMove.IslandChoice, 4);
        q1.add(m1);
        pm1.setMessageQueue(q1);
        playerManagerMap.put(p1.getNickname(), pm1);
        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        assertEquals(0, gameController.getGame().getIslands().get(4).getNumNoEntryTile());

        card5Effect.doEffect();

        assertEquals(1, gameController.getGame().getIslands().get(4).getNumNoEntryTile());
    }

    @Test
    public void testAddNoEntryTile(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);


        //creation of Card5Effect
        CharacterStateNoEntryTile c5 = new CharacterStateNoEntryTile(5, 2);
        Card5Effect card5Effect = new Card5Effect(gameController, c5);

        assertEquals(4, c5.getNumNoEntry());

        card5Effect.addNoEntryTile();

        assertEquals(5, c5.getNumNoEntry());
    }

    @Test
    public void testEffectInfluece(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);


        //creation of Card5Effect
        CharacterStateNoEntryTile c5 = new CharacterStateNoEntryTile(5, 2);
        Card5Effect card5Effect = new Card5Effect(gameController, c5);

        //crearion of Island
        Island island = new Island();

        Player player = card5Effect.effectInfluence(island);
        assertEquals(null, player);
    }


}
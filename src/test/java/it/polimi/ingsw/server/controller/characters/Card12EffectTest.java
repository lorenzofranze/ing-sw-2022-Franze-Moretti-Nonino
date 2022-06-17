package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.GameMessage;
import it.polimi.ingsw.common.messages.Message;
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

class Card12EffectTest {

    @Test
    public void testDoEffect(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);

        //creation of Card12Effect
        CharacterState c12 = new CharacterState(12, 3);
        Card12Effect card12Effect = new Card12Effect(gameController, c12);

        //creation of other two Characters and insertion of charaters in game
        CharacterState c1 = new CharacterState(1, 1);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c1);
        gameCharacters.add(c12);
        gameCharacters.add(c3);
        gameController.getGame().setCharacterStates(gameCharacters);

        //creation of players
        Player p1 = gameController.getGame().getPlayers().get(0);
        Player p2 = gameController.getGame().getPlayers().get(1);
        Player p3 = gameController.getGame().getPlayers().get(2);
        p1.addCoins(3);
        p2.addCoins(5);
        p3.addCoins(0);

        //initialize entances, diningroom and professors of Schoolboards
        PawnsMap entrance = new PawnsMap();
        entrance.add(ColourPawn.Blue, 3);
        entrance.add(ColourPawn.Green, 3);
        entrance.add(ColourPawn.Pink, 1);
        p1.getSchoolBoard().setEntrance(entrance);
        PawnsMap diningRoom = new PawnsMap();
        diningRoom.add(ColourPawn.Blue, 4);
        diningRoom.add(ColourPawn.Green, 3);
        diningRoom.add(ColourPawn.Pink, 1);
        p1.getSchoolBoard().setDiningRoom(diningRoom);
        entrance = new PawnsMap();
        entrance.add(ColourPawn.Blue, 4);
        entrance.add(ColourPawn.Green, 3);
        entrance.add(ColourPawn.Pink, 0);
        p2.getSchoolBoard().setEntrance(entrance);
        diningRoom = new PawnsMap();
        diningRoom.add(ColourPawn.Blue, 4);
        diningRoom.add(ColourPawn.Yellow, 5);
        diningRoom.add(ColourPawn.Pink, 1);
        p2.getSchoolBoard().setDiningRoom(diningRoom);
        p2.getSchoolBoard().getProfessors().add(ColourPawn.Yellow);
        p2.getSchoolBoard().getProfessors().add(ColourPawn.Blue);
        entrance = new PawnsMap();
        entrance.add(ColourPawn.Yellow, 1);
        entrance.add(ColourPawn.Green, 2);
        entrance.add(ColourPawn.Red, 2);
        p3.getSchoolBoard().setEntrance(entrance);
        diningRoom = new PawnsMap();
        diningRoom.add(ColourPawn.Blue, 2);
        diningRoom.add(ColourPawn.Pink, 3);
        p3.getSchoolBoard().setDiningRoom(diningRoom);
        p3.getSchoolBoard().getProfessors().add(ColourPawn.Pink);

        //p1 is the current player
        gameController.setCurrentPlayer(p1);
        PawnsMap diningBefore = p1.getSchoolBoard().getDiningRoom().clone();

        //creation of playerManagerMap
        Map<String, PlayerManager> playerManagerMap = new HashMap<>();
        PlayerManager pm1 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q1= new LinkedBlockingQueue<>();
        PlayerManager pm2 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q2= new LinkedBlockingQueue<>();
        PlayerManager pm3 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q3= new LinkedBlockingQueue<>();

        //DIFFERENT CASES

        //1) colour doesn't exist
        Message m1 = new GameMessage(TypeOfMove.StudentColour, 7);
        q1.add(m1);
        //2) valid movement
        m1 = new GameMessage(TypeOfMove.StudentColour, ColourPawn.Blue.getIndexColour());
        q1.add(m1);
        pm1.setMessageQueue(q1);
        playerManagerMap.put(p1.getNickname(), pm1);
        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        assertEquals(4, p1.getSchoolBoard().getDiningRoom().get(ColourPawn.Blue));
        assertEquals(4, p2.getSchoolBoard().getDiningRoom().get(ColourPawn.Blue));
        assertEquals(2, p3.getSchoolBoard().getDiningRoom().get(ColourPawn.Blue));

        card12Effect.doEffect();

        assertEquals(1, p1.getSchoolBoard().getDiningRoom().get(ColourPawn.Blue));
        assertEquals(1, p2.getSchoolBoard().getDiningRoom().get(ColourPawn.Blue));
        assertEquals(0, p3.getSchoolBoard().getDiningRoom().get(ColourPawn.Blue));

    }

    @Test
    public void testEffectInfluence(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);

        //creation of Card12Effect
        CharacterState c12 = new CharacterStateStudent(12, 3);
        Card12Effect card12Effect = new Card12Effect(gameController, c12);

        //crearion of Island
        Island island = new Island();

        Player player = card12Effect.effectInfluence(island);
        assertEquals(null, player);
    }

}
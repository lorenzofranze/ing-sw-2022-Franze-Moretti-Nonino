package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.GameMessage;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.common.messages.TypeOfMove;
import it.polimi.ingsw.server.controller.logic.ActionPhase;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.PawnsMap;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class Card9EffectTest {

    @Test
    public void testDoEffect(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);
        ActionPhase actionPhase = new ActionPhase(gameController);
        gameController.setActionPhase(actionPhase);

        //creation of Card9Effect
        CharacterState c9 = new CharacterState(9, 3);
        Card9Effect card9Effect = new Card9Effect(gameController, c9);
        List<CharacterEffect> characterEffects = new ArrayList<>();
        characterEffects.add(card9Effect);
        gameController.setCharacterEffects(characterEffects);

        //creation of other two Characters and insertion of charaters in game
        CharacterState c1 = new CharacterState(1, 1);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c1);
        gameCharacters.add(c9);
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

        //1) the colour is invalid
        Message m1 = new GameMessage(TypeOfMove.StudentColour, 5);
        q1.add(m1);
        //2) valid colour
        m1 = new GameMessage(TypeOfMove.StudentColour, ColourPawn.Yellow.getIndexColour());
        q1.add(m1);
        pm1.setMessageQueue(q1);
        playerManagerMap.put(p1.getNickname(), pm1);
        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        card9Effect.doEffect();

        assertEquals(ColourPawn.Yellow, card9Effect.colourPawn);

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
        ActionPhase actionPhase = new ActionPhase(gameController);
        gameController.setActionPhase(actionPhase);

        //creation of Card9Effect
        CharacterState c9 = new CharacterState(9, 3);
        Card9Effect card9Effect = new Card9Effect(gameController, c9);
        List<CharacterEffect> characterEffects = new ArrayList<>();
        characterEffects.add(card9Effect);
        gameController.setCharacterEffects(characterEffects);

        //creation of other two Characters and insertion of charaters in game
        CharacterState c1 = new CharacterState(1, 1);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c1);
        gameCharacters.add(c9);
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
        p1.getSchoolBoard().getProfessors().add(ColourPawn.Blue);
        entrance = new PawnsMap();
        entrance.add(ColourPawn.Blue, 4);
        entrance.add(ColourPawn.Green, 3);
        entrance.add(ColourPawn.Pink, 0);
        p2.getSchoolBoard().setEntrance(entrance);
        diningRoom = new PawnsMap();
        diningRoom.add(ColourPawn.Yellow, 3);
        diningRoom.add(ColourPawn.Pink, 1);
        p2.getSchoolBoard().setDiningRoom(diningRoom);
        p2.getSchoolBoard().getProfessors().add(ColourPawn.Yellow);
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

        //setting of islands
        Island island10 = gameController.getGame().getIslands().get(10);
        PawnsMap studentsOnIsland = new PawnsMap();
        island10.getStudents().setNumberForColour(ColourPawn.Blue, 2);
        island10.getStudents().setNumberForColour(ColourPawn.Yellow, 3);
        island10.setTowerColor(p2.getColourTower());

        //creation of playerManagerMap
        Map<String, PlayerManager> playerManagerMap = new HashMap<>();
        PlayerManager pm1 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q1= new LinkedBlockingQueue<>();
        PlayerManager pm2 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q2= new LinkedBlockingQueue<>();
        PlayerManager pm3 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q3= new LinkedBlockingQueue<>();

        //DIFFERENT CASES

        //1) the colour is invalid
        Message m1 = new GameMessage(TypeOfMove.StudentColour, 5);
        q1.add(m1);
        //2) valid colour
        m1 = new GameMessage(TypeOfMove.StudentColour, ColourPawn.Yellow.getIndexColour());
        q1.add(m1);
        pm1.setMessageQueue(q1);
        playerManagerMap.put(p1.getNickname(), pm1);
        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        //before activating the effect p2 should be the most influent
        Player moreInfluent = actionPhase.calcultateInfluence(island10);
        assertEquals(p2, moreInfluent);

        gameController.getGame().setActiveEffect(c9);
        card9Effect.doEffect();

        //after activating the effect p1 should be the most influent
        moreInfluent = actionPhase.calcultateInfluence(island10);
        assertEquals(p1, moreInfluent);
    }

}
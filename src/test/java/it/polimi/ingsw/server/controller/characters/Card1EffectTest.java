package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
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

class Card1EffectTest {

    @Test
    public void testCard1Effect(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);

        int studentsBagBefore = gameController.getGame().getStudentsBag().pawnsNumber();

        //creation of Card1Effect
        CharacterStateStudent characterStateStudent = new CharacterStateStudent(1, 1);
        Card1Effect card1Effect = new Card1Effect(gameController, characterStateStudent);
        int studentsBagAfter = gameController.getGame().getStudentsBag().pawnsNumber();

        //verify that there are 4 students on the card
        assertEquals(true, card1Effect.characterState instanceof CharacterStateStudent);
        assertEquals(4, ((CharacterStateStudent)(card1Effect.characterState)).getAllStudents().pawnsNumber());
        assertEquals(4, studentsBagBefore-studentsBagAfter);
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

        //creation of Card1Effect
        CharacterStateStudent c1 = new CharacterStateStudent(1, 1);
        Card1Effect card1Effect = new Card1Effect(gameController, c1);
        PawnsMap studentsOnCard = c1.getAllStudents().clone();
        ColourPawn colourPresent = null;
        ColourPawn colourAbsent = null;
        for (ColourPawn c : ColourPawn.values()){
            if (studentsOnCard.get(c) > 0){
                colourPresent = c;
            }
            else{
                colourAbsent = c;
            }
        }

        //creation of other two Characters and insertion of charaters in game
        CharacterState c2 = new CharacterState(2, 2);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c1);
        gameCharacters.add(c2);
        gameCharacters.add(c3);
        gameController.getGame().setCharacterStates(gameCharacters);
        int studentsBagBefore = gameController.getGame().getStudentsBag().pawnsNumber();
        Island island10 = gameController.getGame().getIslandOfIndex(10);
        PawnsMap pawnsIsland10Before = island10.getStudents().clone();

        //creation of players
        Player p1 = gameController.getGame().getPlayers().get(0);
        Player p2 = gameController.getGame().getPlayers().get(1);
        Player p3 = gameController.getGame().getPlayers().get(2);
        p1.addCoins(3);
        p2.addCoins(5);
        p3.addCoins(0);

        //initialize entances of Schoolboards
        PawnsMap entrance = new PawnsMap();
        entrance.add(ColourPawn.Blue, 3);
        entrance.add(ColourPawn.Green, 3);
        entrance.add(ColourPawn.Pink, 1);
        p1.getSchoolBoard().setEntrance(entrance);
        entrance = new PawnsMap();
        entrance.add(ColourPawn.Blue, 4);
        entrance.add(ColourPawn.Green, 3);
        entrance.add(ColourPawn.Pink, 0);
        p2.getSchoolBoard().setEntrance(entrance);
        entrance = new PawnsMap();
        entrance.add(ColourPawn.Yellow, 1);
        entrance.add(ColourPawn.Green, 2);
        entrance.add(ColourPawn.Red, 2);
        p3.getSchoolBoard().setEntrance(entrance);

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

        //1) colour doesn't exist
        Message m1 = new PawnMovementMessage(7, 4);
        q1.add(m1);
        //2) the card doesn't have that colour
        m1 = new PawnMovementMessage(colourAbsent.getIndexColour(), 4);
        q1.add(m1);
        //3) the island is invalid
        m1 = new PawnMovementMessage(colourPresent.getIndexColour(), 13);
        q1.add(m1);
        //4) valid movement
        m1 = new PawnMovementMessage(colourPresent.getIndexColour(), 10);
        q1.add(m1);
        pm1.setMessageQueue(q1);
        playerManagerMap.put(p1.getNickname(), pm1);
        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        card1Effect.doEffect();

        int studentsBagAfter = gameController.getGame().getStudentsBag().pawnsNumber();
        assertEquals(1, studentsBagBefore-studentsBagAfter);
        PawnsMap pawnsIsland10After = island10.getStudents().clone();
        pawnsIsland10Before.add(colourPresent, 1);
        assertEquals(pawnsIsland10Before, pawnsIsland10After);
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


        //creation of Card1Effect
        CharacterStateStudent characterStateStudent = new CharacterStateStudent(1, 1);
        Card1Effect card1Effect = new Card1Effect(gameController, characterStateStudent);

        //crearion of Island
        Island island = new Island();

        Player player = card1Effect.effectInfluence(island);
        assertEquals(null, player);
    }




}
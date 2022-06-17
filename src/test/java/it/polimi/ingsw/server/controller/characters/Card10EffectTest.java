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
import it.polimi.ingsw.server.model.*;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class Card10EffectTest {

    @Test
    public void testEffectInfluence(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);


        //creation of Card10Effect
        CharacterState c10 = new CharacterState(10, 1);
        Card10Effect card10Effect = new Card10Effect(gameController, c10);

        //creation of Island
        Island island = new Island();

        Player player = card10Effect.effectInfluence(island);
        assertEquals(null, player);
    }

    @Test
    public void testDoEffect() {

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);
        ActionPhase actionPhase = new ActionPhase(gameController);
        gameController.setActionPhase(actionPhase);

        //creation of Card10Effect
        CharacterState c10 = new CharacterStateStudent(10, 1);
        Card10Effect card10Effect = new Card10Effect(gameController, c10);
        List<CharacterEffect> characterEffects = new ArrayList<>();
        characterEffects.add(card10Effect);
        gameController.setCharacterEffects(characterEffects);

        //creation of other two Characters and insertion of charaters in game
        CharacterState c1 = new CharacterState(1, 1);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c1);
        gameCharacters.add(c10);
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
        diningRoom.add(ColourPawn.Pink, 10);
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
        PawnsMap entranceBefore = p1.getSchoolBoard().getEntrance().clone();
        PawnsMap diningBefore = p1.getSchoolBoard().getDiningRoom().clone();
        PawnsMap totalStudentsBefore = new PawnsMap();
        totalStudentsBefore.add(entranceBefore);
        totalStudentsBefore.add(diningBefore);

        //creation of playerManagerMap
        Map<String, PlayerManager> playerManagerMap = new HashMap<>();
        PlayerManager pm1 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q1= new LinkedBlockingQueue<>();
        PlayerManager pm2 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q2= new LinkedBlockingQueue<>();
        PlayerManager pm3 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q3= new LinkedBlockingQueue<>();

        //finding two students that are on the player DiningRoom, one that is not and one whose dining table is full
        ColourPawn presentDining1 = null;
        ColourPawn presentDining2 = null;
        ColourPawn fullDining = null;
        ColourPawn absentDining1 = null;
        for (ColourPawn c : ColourPawn.values()){
            if (diningBefore.get(c) > 0 && diningBefore.get(c) != 10){
                if (presentDining1 == null){
                    presentDining1 = c;
                }
                else {
                    presentDining2 = c;
                }
            }
            if (diningBefore.get(c) == 10){
                fullDining = c;
            }
            if (diningBefore.get(c) == 0){
                absentDining1 = c;
            }
        }

        //finding two students that are on the player entrance and one that is not
        ColourPawn presentEntrance1 = null;
        ColourPawn presentEntrance2 = null;
        ColourPawn absentEntrance1 = null;
        for (ColourPawn c : ColourPawn.values()){
            if (entranceBefore.get(c) > 0){
                if (presentEntrance1 == null){
                    presentEntrance1 = c;
                }
                else {
                    presentEntrance2 = c;
                }
            }
            else{
                absentEntrance1 = c;
            }
        }

        //DIFFERENT CASES

        //1) the number of pawns to move is invalid
        Message m1 = new GameMessage(TypeOfMove.NumOfMove, 3);
        q1.add(m1);
        //2) valid number of pawns to move
        m1 = new GameMessage(TypeOfMove.NumOfMove, 2);
        q1.add(m1);
        //3) the colour chosen is invalid
        m1 = new GameMessage(TypeOfMove.StudentColour, 6);
        q1.add(m1);
        //4) the player hasn't got the colour in his entrance
        m1 = new GameMessage(TypeOfMove.StudentColour, absentEntrance1.getIndexColour());
        q1.add(m1);
        //5) the player dining is full
        m1 = new GameMessage(TypeOfMove.StudentColour, fullDining.getIndexColour());
        q1.add(m1);
        //6) valid colour on entrance 1
        m1 = new GameMessage(TypeOfMove.StudentColour, presentEntrance1.getIndexColour());
        q1.add(m1);
        //7) invalid colour in dining
        m1 = new GameMessage(TypeOfMove.StudentColour, 6);
        q1.add(m1);
        //8) the player hasn't got the colour in his diningroom
        m1 = new GameMessage(TypeOfMove.StudentColour, absentDining1.getIndexColour());
        q1.add(m1);
        //9) valid colour on dining 1
        m1 = new GameMessage(TypeOfMove.StudentColour, presentEntrance2.getIndexColour());
        q1.add(m1);
        //10) valid colour on entrance 2
        m1 = new GameMessage(TypeOfMove.StudentColour, presentEntrance2.getIndexColour());
        q1.add(m1);
        //11) valid colour on dining 2
        m1 = new GameMessage(TypeOfMove.StudentColour, presentDining2.getIndexColour());
        q1.add(m1);
        pm1.setMessageQueue(q1);
        playerManagerMap.put(p1.getNickname(), pm1);
        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        card10Effect.doEffect();

        PawnsMap entranceAfter = p1.getSchoolBoard().getEntrance().clone();
        PawnsMap diningAfter = p1.getSchoolBoard().getDiningRoom().clone();
        PawnsMap totalStudentsAfter = new PawnsMap();
        totalStudentsAfter.add(entranceAfter);
        totalStudentsAfter.add(diningAfter);

        assertEquals(totalStudentsAfter, totalStudentsBefore);

    }

}
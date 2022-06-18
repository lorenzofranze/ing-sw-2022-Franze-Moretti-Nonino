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

class Card7EffectTest {

    @Test
    public void testCard7Effect() {

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);

        int studentsBagBefore = gameController.getGame().getStudentsBag().pawnsNumber();

        //creation of Card7Effect
        CharacterStateStudent characterStateStudent = new CharacterStateStudent(7, 1);
        Card7Effect card7Effect = new Card7Effect(gameController, characterStateStudent);
        int studentsBagAfter = gameController.getGame().getStudentsBag().pawnsNumber();

        //verify that there are 4 students on the card
        assertEquals(true, card7Effect.characterState instanceof CharacterStateStudent);
        assertEquals(6, ((CharacterStateStudent) (card7Effect.characterState)).getAllStudents().pawnsNumber());
        assertEquals(6, studentsBagBefore - studentsBagAfter);
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

        //creation of Card7Effect
        CharacterStateStudent c7 = new CharacterStateStudent(7, 1);
        Card7Effect card7Effect = new Card7Effect(gameController, c7);
        List<CharacterEffect> characterEffects = new ArrayList<>();
        characterEffects.add(card7Effect);
        gameController.setCharacterEffects(characterEffects);
        PawnsMap studentsOnCard = c7.getAllStudents().clone();

        //creation of other two Characters and insertion of charaters in game
        CharacterState c1 = new CharacterState(1, 1);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c1);
        gameCharacters.add(c7);
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
        PawnsMap entranceBefore = p1.getSchoolBoard().getEntrance().clone();

        //creation of playerManagerMap
        Map<String, PlayerManager> playerManagerMap = new HashMap<>();
        PlayerManager pm1 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q1= new LinkedBlockingQueue<>();
        PlayerManager pm2 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q2= new LinkedBlockingQueue<>();
        PlayerManager pm3 = new PlayerManager(null, null);
        LinkedBlockingQueue<Message> q3= new LinkedBlockingQueue<>();

        //finding two students that are on the card and one that is not
        ColourPawn presentCard1 = null;
        ColourPawn presentCard2 = null;
        ColourPawn absentCard1 = null;
        for (ColourPawn c : ColourPawn.values()){
            if (studentsOnCard.get(c) > 0){
                if (presentCard1 == null){
                    presentCard1 = c;
                }
                else {
                    presentCard2 = c;
                }
            }
            else{
                absentCard1 = c;
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
        Message m1 = new GameMessage(TypeOfMove.NumOfMove, 4);
        q1.add(m1);
        //2) valid number of pawns to move
        m1 = new GameMessage(TypeOfMove.NumOfMove, 2);
        q1.add(m1);
        if (absentCard1!=null){
            //3) the colour chosen is not on the card
            m1 = new GameMessage(TypeOfMove.StudentColour, absentCard1.getIndexColour());
            q1.add(m1);
        }
        //4) valid colour on card 1
        m1 = new GameMessage(TypeOfMove.StudentColour, presentCard1.getIndexColour());
        q1.add(m1);
        //5) the colour chosen is not on the entrance
        m1 = new GameMessage(TypeOfMove.StudentColour, absentEntrance1.getIndexColour());
        q1.add(m1);
        //6) valid colour on entrance 1
        m1 = new GameMessage(TypeOfMove.StudentColour, presentEntrance1.getIndexColour());
        q1.add(m1);
        //7) valid colour on card 2
        m1 = new GameMessage(TypeOfMove.StudentColour, presentCard2.getIndexColour());
        q1.add(m1);
        //8) valid colour on entrance 2
        m1 = new GameMessage(TypeOfMove.StudentColour, presentEntrance2.getIndexColour());
        q1.add(m1);
        pm1.setMessageQueue(q1);
        playerManagerMap.put(p1.getNickname(), pm1);
        gameController.getMessageHandler().setPlayerManagerMap(playerManagerMap);

        card7Effect.doEffect();

        PawnsMap studentsOnCardAfter = c7.getAllStudents().clone();
        PawnsMap totalStudents = new PawnsMap();
        totalStudents.add(studentsOnCardAfter);
        totalStudents.add(p1.getSchoolBoard().getEntrance());
        studentsOnCard.add(entranceBefore);

        assertEquals(studentsOnCard, totalStudents);

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


        //creation of Card7Effect
        CharacterState c7 = new CharacterStateStudent(3, 3);
        Card7Effect card7Effect = new Card7Effect(gameController, c7);

        //crearion of Island
        Island island = new Island();

        Player player = card7Effect.effectInfluence(island);
        assertEquals(null, player);
    }
}
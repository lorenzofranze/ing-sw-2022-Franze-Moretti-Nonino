package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.server.controller.logic.ActionPhase;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.*;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Card2EffectTest {

    @Test
    public void testCard2Effect(){

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);

        //creation of Card2Effect
        CharacterState characterState = new CharacterState(2, 2);
        Card2Effect card2Effect = new Card2Effect(gameController, characterState);

        //verify that there is a pawnsMap on the card for the stolen professors
        assertEquals(true, card2Effect.stolenProfessors instanceof PawnsMap);
        assertEquals(0, card2Effect.stolenProfessors.pawnsNumber());
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

        //creation of Card2Effect
        CharacterState c2 = new CharacterState(2, 2);
        Card2Effect card2Effect = new Card2Effect(gameController, c2);

        //creation of other two Characters and insertion of charaters in game
        CharacterState c1 = new CharacterState(1, 1);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c1);
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

        //initialize entances, diningroom and professors of Schoolboards
        //p1 and p2 have the same number of blue pawns in diningRoom, but p2 has the blue professor
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

        card2Effect.doEffect();

        //verify that stolenProfessors has a blue professor
        assertEquals(true, card2Effect.stolenProfessors instanceof PawnsMap);
        assertEquals(1, card2Effect.stolenProfessors.pawnsNumber());
        assertEquals(1, card2Effect.stolenProfessors.get(ColourPawn.Blue));
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

        //creation of Card2Effect
        CharacterState c2 = new CharacterState(2, 2);
        Card2Effect card2Effect = new Card2Effect(gameController, c2);
        List<CharacterEffect> characterEffects = new ArrayList<>();
        characterEffects.add(card2Effect);
        gameController.setCharacterEffects(characterEffects);

        //creation of other two Characters and insertion of charaters in game
        CharacterState c1 = new CharacterState(1, 1);
        CharacterState c3 = new CharacterState(3, 3);
        List<CharacterState> gameCharacters= new ArrayList<>();
        gameCharacters.add(c1);
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

        //setting of island
        Island island10 = gameController.getGame().getIslands().get(10);
        PawnsMap studentsOnIsland = new PawnsMap();
        island10.getStudents().setNumberForColour(ColourPawn.Blue, 3);
        island10.getStudents().setNumberForColour(ColourPawn.Yellow, 2);
        island10.setTowerColor(p1.getColourTower());


        //initialize entances, diningroom and professors of Schoolboards
        //p1 and p2 have the same number of blue pawns in diningRoom, but p2 has the blue professor
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

        //before activating the effect p2 should be the most influent
        Player moreInfluent = actionPhase.calcultateInfluence(island10);
        assertEquals(p2, moreInfluent);

        gameController.getGame().setActiveEffect(c2);
        card2Effect.doEffect();

        //verify that stolenProfessors has a blue professor
        assertEquals(true, card2Effect.stolenProfessors instanceof PawnsMap);
        assertEquals(1, card2Effect.stolenProfessors.pawnsNumber());
        assertEquals(1, card2Effect.stolenProfessors.get(ColourPawn.Blue));

        //after activating the effect p1 should be the most influent
        moreInfluent = actionPhase.calcultateInfluence(island10);
        assertEquals(p1, moreInfluent);
    }

}
package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.server.controller.logic.ActionPhase;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.logic.PianificationResult;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.CharacterStateStudent;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Card4EffectTest {

    @Test
    public void testCard4Effect() {

        //creation of gameController
        ArrayList<String> players = new ArrayList<>();
        Lobby lobby = new Lobby(GameMode.Complex_3);
        lobby.addUsersReadyToPlay("vale", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("lara", new Socket(), new PlayerManager(null, null));
        lobby.addUsersReadyToPlay("franzo", new Socket(), new PlayerManager(null, null));
        GameController gameController = new GameController(lobby, true);

        int studentsBagBefore = gameController.getGame().getStudentsBag().pawnsNumber();

        //creation of Card4Effect
        CharacterState c4 = new CharacterStateStudent(4, 1);
        Card4Effect card4Effect = new Card4Effect(gameController, c4);
        int studentsBagAfter = gameController.getGame().getStudentsBag().pawnsNumber();

        //verify that there are 4 students on the card
        assertEquals(true, card4Effect.characterState instanceof CharacterState);
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

        //creation of Card4Effect
        CharacterState c4 = new CharacterState(4, 1);
        Card4Effect card4Effect = new Card4Effect(gameController, c4);

        //creation of other two Characters and insertion of charaters in game
        CharacterState c1 = new CharacterState(1, 1);
        CharacterState c2 = new CharacterState(2, 2);
        List<CharacterState> gameCharacters = new ArrayList<>();
        gameCharacters.add(c1);
        gameCharacters.add(c2);
        gameCharacters.add(c4);
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

        //results from pianificationPhase
        HashMap<Player, Integer> maximumMovements = new HashMap<>();
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(p2);
        turnOrder.add(p3);
        turnOrder.add(p1);
        actionPhase.setTurnOrder(turnOrder);
        maximumMovements.put(p1, 3);
        maximumMovements.put(p2, 2);
        maximumMovements.put(p3, 2);
        actionPhase.setMaximumMovements(maximumMovements);

        int maxMovemetsBefore = gameController.getActionPhase().getMaximumMovements().get(p1);

        card4Effect.doEffect();

        int maxMovemetsAfter = gameController.getActionPhase().getMaximumMovements().get(p1);

        assertEquals(2, maxMovemetsAfter-maxMovemetsBefore);
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


        //creation of Card4Effect
        CharacterState c4 = new CharacterState(3, 3);
        Card4Effect card4Effect = new Card4Effect(gameController, c4);

        //crearion of Island
        Island island = new Island();

        Player player = card4Effect.effectInfluence(island);
        assertEquals(null, player);
    }

}
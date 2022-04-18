package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Controller.Characters.*;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Character;

import java.util.*;

public class SetUpPhase extends GamePhase {
    private final GameController gameController;

    public SetUpPhase(GameController gameController) {
        this.gameController = gameController;
    }

    public SetUpResult handle() {
        this.placePawnsIslands();
        this.fillSchoolBoard();

        Player p = chooseFirstPianificationPlayer();
        SetUpResult ris = new SetUpResult();
        ris.setFirstRandomPianificationPlayer(p);

        if (gameController.isExpert() == true) {
            this.distributeCoins();
        }

        this.initializeCharactersEffects();

        return ris;

    }

    /**
     * gives one coin per player at the start of the game if the game is expert-mode
     */
    private void distributeCoins() {
        for (Player player : this.gameController.getGame().getPlayers()) {
            player.addCoins(1);
            this.gameController.getGame().removeCoins(1);
        }
    }

    /**
     * Places MotherNature on a random island and 10 students (one of each colour) on the islands leaving the
     * one on the other side of mothernatur empty. Removes the 10 students form the StudentBag.
     */
    private void placePawnsIslands() {

        Random rand = new Random();
        int randomNum = rand.nextInt(12);


        Island islandMotherNature = gameController.getGame().getIslands().get(randomNum);
        islandMotherNature.setHasMotherNature(true);

        int opposite = randomNum + 6;
        if (opposite >= 12) {
            opposite -= 12;
        }

        List<ColourPawn> studentsToPlace = new ArrayList<ColourPawn>(10);
        for (ColourPawn currColor : ColourPawn.values()) {
            studentsToPlace.add(currColor);
            studentsToPlace.add(currColor);
            gameController.getGame().getStudentsBag().remove(currColor, 2);
        }

        Collections.shuffle(studentsToPlace);

        int i = 0;
        for (Island island : gameController.getGame().getIslands()) {
            if (!(island.equals(gameController.getGame().getIslands().get(randomNum))) &&
                    !(island.equals(gameController.getGame().getIslands().get(opposite)))) {
                PawnsMap map = new PawnsMap();
                map.add(studentsToPlace.get(i));
                island.addStudents(map);
                i++;
            }
        }
    }

    /**
     * Places 7 (2 players) or 9 (3 players) random students at the entrance of each player's SchoolBoard.
     */
    private void fillSchoolBoard() {
        int n = 7;
        int towersToRemove = 0;
        if (gameController.getGame().getPlayers().size() == 3) {
            n = 9;
            towersToRemove = 2;
        }

        for (Player player : gameController.getGame().getPlayers()) {
            player.getSchoolBoard().getEntrance().add(gameController.getGame().getStudentsBag().removeRandomly(n));
            for (int i = 0; i < towersToRemove; i++)
                player.getSchoolBoard().removeTower();
        }
    }

    /**
     * returns a random Player
     */
    private Player chooseFirstPianificationPlayer() {
        int n = gameController.getGame().getPlayers().size();
        Random rand = new Random();
        int randomNum = rand.nextInt(n);

        Player ris = gameController.getGame().getPlayers().get(randomNum);
        return ris;
    }

    private void initializeCharactersEffects() {
        for (Character cr : gameController.getGame().getCharacters()) {

            if (cr.getCharacterId() == 1 || cr.getCharacterId() == 5 || cr.getCharacterId() == 6 || cr.getCharacterId() == 11) {
                CharacterEffectInitialize characterEffect = (CharacterEffectInitialize) gameController.getCharacterEffects().get(cr);
                characterEffect.initializeCard();
            }

            if (cr.getCharacterId() == 1) {
                gameController.getCharacterEffects().put(cr, new Card1(gameController));
            }
            if (cr.getCharacterId() == 2) {
                gameController.getCharacterEffects().put(cr, new Card2(gameController));
            }
            //??
            if (cr.getCharacterId() == 3) {
                gameController.getCharacterEffects().put(cr, new Card3(gameController));
            }
            if (cr.getCharacterId() == 4) {
                gameController.getCharacterEffects().put(cr, new Card4(gameController));
            }
            if (cr.getCharacterId() == 5) {
                gameController.getCharacterEffects().put(cr, new Card5(gameController));
            }
            if (cr.getCharacterId() == 6) {
                gameController.getCharacterEffects().put(cr, new Card6(gameController));
            }
            if (cr.getCharacterId() == 7) {
                gameController.getCharacterEffects().put(cr, new Card7(gameController));
            }
            if (cr.getCharacterId() == 8) {
                gameController.getCharacterEffects().put(cr, new Card8(gameController));
            }
            if (cr.getCharacterId() == 9) {
                gameController.getCharacterEffects().put(cr, new Card9(gameController));
            }
            if (cr.getCharacterId() == 10) {
                gameController.getCharacterEffects().put(cr, new Card10(gameController));
            }
            if (cr.getCharacterId() == 11) {
                gameController.getCharacterEffects().put(cr, new Card11(gameController));
            }
            if (cr.getCharacterId() == 12) {
                gameController.getCharacterEffects().put(cr, new Card12(gameController));
            }
        }
    }
}

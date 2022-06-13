package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.ColourTower;
import it.polimi.ingsw.server.controller.characters.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.CharacterState;

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
            this.initializeCharactersEffects();
        }


        return ris;

    }

    /**
     * gives one coin per player at the start of the game if the game is expert-mode
     */
    private void distributeCoins() {
        //todo: for debug
        int i=0;
        for (Player player : this.gameController.getGame().getPlayers()) {
            player.addCoins(3-i);
            this.gameController.getGame().removeCoins(5-i);
            i++;
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

    public void initializeCharactersEffects() {
        for (CharacterState cr : gameController.getGame().getCharacters()) {
            if (cr.getCharacterId() == 1) {gameController.getCharacterEffects().add(new Card1Effect(gameController, cr));}
            if (cr.getCharacterId() == 2) {gameController.getCharacterEffects().add(new Card2Effect(gameController, cr));}
            if (cr.getCharacterId() == 3) {gameController.getCharacterEffects().add(new Card3Effect(gameController, cr));}
            if (cr.getCharacterId() == 4) {gameController.getCharacterEffects().add(new Card4Effect(gameController, cr));}
            if (cr.getCharacterId() == 5) {gameController.getCharacterEffects().add(new Card5Effect(gameController, cr));}
            if (cr.getCharacterId() == 6) {gameController.getCharacterEffects().add(new Card6Effect(gameController, cr));}
            if (cr.getCharacterId() == 7) {gameController.getCharacterEffects().add(new Card7Effect(gameController, cr));}
            if (cr.getCharacterId() == 8) {gameController.getCharacterEffects().add(new Card8Effect(gameController, cr));}
            if (cr.getCharacterId() == 9) {gameController.getCharacterEffects().add(new Card9Effect(gameController, cr));}
            if (cr.getCharacterId() == 10) {gameController.getCharacterEffects().add(new Card10Effect(gameController, cr));}
            if (cr.getCharacterId() == 11) {gameController.getCharacterEffects().add(new Card11Effect(gameController, cr));}
            if (cr.getCharacterId() == 12) {gameController.getCharacterEffects().add(new Card12Effect(gameController, cr));}
        }
    }

    @Override
    public String toString(){
        return "SetUpPhase";
    }
}

package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Controller.Characters.Card1;
import it.polimi.ingsw.Server.Controller.Characters.Card2;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Character;

import java.util.*;

public class SetUpPhase extends GamePhase {
    private final GameController gameController;

    public SetUpPhase(GameController gameController){
        this.gameController = gameController;
    }

    public SetUpResult handle(){
        this.placePawnsIslands();
        this.fillSchoolBoard();

        Player p = chooseFirstPianificationPlayer();
        SetUpResult ris= new SetUpResult();
        ris.setFirstRandomPianificationPlayer(p);

        if(gameController.isExpert()==true){
            this.distributeCoins();
        }

        this.initializeCharactersEffects();

        return ris;

    }

    /** gives one coin per player at the start of the game if the game is expert-mode
     */
    private void distributeCoins(){
        for(Player player: this.gameController.getGame().getPlayers()){
            player.addCoins(1);
            this.gameController.getGame().removeCoins(1);
        }
    }

    /**Places MotherNature on a random island and 10 students (one of each colour) on the islands leaving the
     *  one on the other side of mothernatur empty. Removes the 10 students form the StudentBag.*/
    private void placePawnsIslands(){

        Random rand = new Random();
        int randomNum = rand.nextInt(12);


        Island islandMotherNature = gameController.getGame().getIslands().get(randomNum);
        islandMotherNature.setHasMotherNature(true);

        int opposite = randomNum + 6;
        if (opposite >= 12) {opposite -= 12;}

        List<ColourPawn> studentsToPlace = new ArrayList<ColourPawn>(10);
        for (ColourPawn currColor: ColourPawn.values()){
            studentsToPlace.add(currColor);
            studentsToPlace.add(currColor);
            gameController.getGame().getStudentsBag().remove(currColor, 2);
        }

        Collections.shuffle(studentsToPlace);

        int i = 0;
        for(Island island: gameController.getGame().getIslands()){
            if (!(island.equals(gameController.getGame().getIslands().get(randomNum))) &&
                    !(island.equals(gameController.getGame().getIslands().get(opposite)))){
                PawnsMap map = new PawnsMap();
                map.add(studentsToPlace.get(i));
                island.addStudents(map);
                i++;
            }
        }
    }

    /**Places 7 (2 players) or 9 (3 players) random students at the entrance of each player's SchoolBoard.*/
    private void fillSchoolBoard(){
        int n = 7;
        int towersToRemove = 0;
        if (gameController.getGame().getPlayers().size() == 3) {n = 9; towersToRemove=2;}

        for (Player player : gameController.getGame().getPlayers()) {
            player.getSchoolBoard().getEntrance().add(gameController.getGame().getStudentsBag().removeRandomly(n));
            for(int i=0; i<towersToRemove; i++)
                player.getSchoolBoard().removeTower();
        }
    }

    /**returns a random Player*/
    private Player chooseFirstPianificationPlayer(){

        int n = gameController.getGame().getPlayers().size();
        Random rand = new Random();
        int randomNum = rand.nextInt(n);

        Player ris = gameController.getGame().getPlayers().get(randomNum);
        return ris;
    }

    private void initializeCharactersEffects(){

        for(Character cr: gameController.getGame().getCharacters()){
            if(cr.getCharacterId()==1) {
                gameController.getCharacterEffects().put(cr, new Card1(gameController));
                gameController.getCharacterEffects().get(cr).initializeCard();
            }
            if(cr.getCharacterId()==2){
                gameController.getCharacterEffects().put(cr, new Card2(gameController));
                gameController.getCharacterEffects().get(cr).initializeCard();
            }
            // da aggiungere altri
        }
    }
}

package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SetUpPhase implements GamePhase {
    private final GameController gameController;

    public SetUpPhase(GameController gameController){
        this.gameController = gameController;
    }

    @Override
    public void handle(){
    }

    /**Places MotherNature on a random island and 10 students (one of each colour) on the islands leaving the
     *  one on the other side of mothernatur empty. Removes the 10 students form the StudentBag.*/
    private void placePawnsIslands(){

        Random rand = new Random();
        int randomNum = rand.nextInt(12);


        Island islandMotherNature = gameController.getGame().getIslands().get(randomNum);
        islandMotherNature.setHasMotherNature(true);

        int opposite = randomNum + 6;
        if (opposite > 12) {opposite -= 12;}

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
    private void fillEntrances(){


        int n = 7;
        if (gameController.getGame().getPlayers().size() == 3) {n = 9;}

        for (Player player : gameController.getGame().getPlayers()) {
            player.getSchoolBoard().getEntrance().add(gameController.getGame().getStudentsBag().removeRandomly(n));
        }

    }

    /**Sets the attribute controller.firstPianificationPlayer that decides who is the first player of the PianificationPhase.*/
    private void setFirstPianificationPlayer(){


        int n = gameController.getGame().getPlayers().size();
        Random rand = new Random();
        int randomNum = rand.nextInt(n);

        Player ris = gameController.getGame().getPlayers().get(randomNum);
        gameController.setFirstPianificationPlayer(ris);

    }

}

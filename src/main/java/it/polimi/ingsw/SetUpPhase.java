package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SetUpPhase implements GamePhase{

    @Override
    public void handle(){}

    /**Places MotherNature on a random island and 10 students (one of each colour) on the islands leaving the
     *  one on the other side of mothernatur empty. Removes the 10 students form the StudentBag.*/
    private void placePawnsIslands(){

        Random rand = new Random();
        int randomNum = rand.nextInt(12);

        Controller controller = Controller.getIntance();

        Island islandMotherNature = controller.getGame().getIslands().get(randomNum);
        islandMotherNature.setHasMotherNature(true);

        int opposite = randomNum + 6;
        if (opposite > 12) {opposite -= 12;}

        List<ColourPawn> studentsToPlace = new ArrayList<ColourPawn>(10);
        for (ColourPawn currColor: ColourPawn.values()){
            studentsToPlace.add(currColor);
            studentsToPlace.add(currColor);
            controller.getGame().getStudentsBag().removePawns(currColor, 2);
        }

        Collections.shuffle(studentsToPlace);

        int i = 0;
        for(Island island: controller.getGame().getIslands()){
            if (!(island.equals(controller.getGame().getIslands().get(randomNum))) &&
                    !(island.equals(controller.getGame().getIslands().get(opposite)))){
            island.getStudents().addPawn(studentsToPlace.get(i));
            i++;
            }
        }
    }

    /**Places 7 (2 players) or 9 (3 players) random students at the entrance of each player's SchoolBoard.*/
    private void fillEntrances(){

        Controller controller = Controller.getIntance();

        int n = 7;
        if (controller.getGame().getPlayers().size() == 3) {n = 9;}

        ColourPawn toAdd;
        for (Player player : controller.getGame().getPlayers()){
            for (int i = 0; i < n; i++) {
                toAdd = controller.getGame().getStudentsBag().removePawnRandomly();
                player.getSchoolBoard().getEntrance().addPawn(toAdd);
            }

        }
    }

    /**Sets the attribute controller.firstPianificationPlayer that decides who is the first player of the PianificationPhase.*/
    private void setFirstPianificationPlayer(){

        Controller controller = Controller.getIntance();

        int n = controller.getGame().getPlayers().size();
        Random rand = new Random();
        int randomNum = rand.nextInt(n);

        Player ris = controller.getGame().getPlayers().get(randomNum);
        controller.setFirstPianificationPlayer(ris);

    }

}

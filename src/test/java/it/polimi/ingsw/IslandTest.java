package it.polimi.ingsw;

import it.polimi.ingsw.Server.Model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    @Test
    public void testSetHasMotherNature() {
        Island island=new Island();
        island.setHasMotherNature(true);
        assertEquals(true, island.getHasMotherNature());
        island.setHasMotherNature(false);
        assertEquals(false, island.getHasMotherNature());
    }

    @Test
    public void testSetTowerColor() {
        Island island=new Island();
        island.setTowerColor(ColourTower.Grey);
        assertEquals(island.getTowerColour(), ColourTower.Grey);
    }
    @Test
    public void testAddTower(){
        Island island=new Island();
        island.setTowerColor(ColourTower.White);
        island.addTower(3);
        assertEquals(3,island.getTowerCount());
    }

    @Test
    public void testAddStudents() {
        Island island=new Island();
        island.getStudents().add(ColourPawn.Green, 3);
        island.getStudents().add(ColourPawn.Pink, 6);
        assertEquals(3,island.getStudents().get(ColourPawn.Green));
        assertEquals(6,island.getStudents().get(ColourPawn.Pink));
        assertEquals(0,island.getStudents().get(ColourPawn.Blue));
        assertEquals(0,island.getStudents().get(ColourPawn.Red));
        assertEquals(0,island.getStudents().get(ColourPawn.Blue));
    }

    @Test
    public void testGetInfluence(){

        Island island=new Island();
        island.getStudents().add(ColourPawn.Green, 3);
        island.getStudents().add(ColourPawn.Pink, 6);
        island.getStudents().add(ColourPawn.Red, 4);
        island.getStudents().add(ColourPawn.Yellow, 2);
        island.setTowerColor(ColourTower.Grey);
        island.addTower(3);
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Lara");
        listPlayer.add("Lorenzo");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        listTower.add(ColourTower.Grey);
        listTower.add(ColourTower.Black);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);
        listWizard.add(ColourWizard.Green);
        listWizard.add(ColourWizard.Violet);
        Game game=new Game(listPlayer, 2);

        game.getPlayers().get(0).getSchoolBoard().getProfessors().add(ColourPawn.Yellow);
        game.getPlayers().get(1).getSchoolBoard().getProfessors().add(ColourPawn.Pink);
        game.getPlayers().get(1).getSchoolBoard().getProfessors().add(ColourPawn.Green);
        assertEquals(game.getPlayers().get(1), island.getInfluence(game));
    }

    @Test
    public void testGetInfluenceNull(){
        Island island=new Island();
    }

}

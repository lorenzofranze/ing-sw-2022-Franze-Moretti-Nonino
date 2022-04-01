package it.polimi.ingsw;

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
    public void testSetHasNoEntryTile() {
        Island island=new Island();
        island.setHasNoEntryTile(true);
        assertEquals(true, island.getHasNoEntryTile());
        island.setHasNoEntryTile(false);
        assertEquals(false, island.getHasNoEntryTile());
    }

    @Test
    public void testAddStudents() {
        Island island=new Island();
        island.addStudents(ColourPawn.Green, 3);
        island.addStudents(ColourPawn.Pink, 6);
        assertEquals(3,island.getStudents().get(ColourPawn.Green));
        assertEquals(6,island.getStudents().get(ColourPawn.Pink));
        assertEquals(0,island.getStudents().get(ColourPawn.Blue));
        assertEquals(0,island.getStudents().get(ColourPawn.Red));
        assertEquals(0,island.getStudents().get(ColourPawn.Blue));
    }

    @Test
    public void testGetInfluence(){

        Island island=new Island();
        island.addStudents(ColourPawn.Green, 3);
        island.addStudents(ColourPawn.Pink, 6);
        island.addStudents(ColourPawn.Red, 4);
        island.addStudents(ColourPawn.Yellow, 2);
        island.setTowerColor(ColourTower.Grey);
        island.addTower(3);
        Player p1=new Player("p1",ColourTower.Grey, ColourWizard.Green);
        Player p2=new Player("p2",ColourTower.Black, ColourWizard.Blue);
        Player p3=new Player("p3",ColourTower.White, ColourWizard.Violet);
        ArrayList<Player> list= new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        Game game=new Game(list,false);
        p1.getSchoolBoard().addProfessor(ColourPawn.Yellow);
        p2.getSchoolBoard().addProfessor(ColourPawn.Pink);
        p2.getSchoolBoard().addProfessor(ColourPawn.Green);
        assertEquals(p2, island.getInfluence(game));
    }

    @Test
    public void testGetInfluenceNull(){
        Island island=new Island();
    }


}

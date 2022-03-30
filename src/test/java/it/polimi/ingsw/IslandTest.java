package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

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
        assertEquals(3,island.getStudents().getPawns(ColourPawn.Green));
        assertEquals(6,island.getStudents().getPawns(ColourPawn.Pink));
        assertEquals(0,island.getStudents().getPawns(ColourPawn.Blue));
        assertEquals(0,island.getStudents().getPawns(ColourPawn.Red));
        assertEquals(0,island.getStudents().getPawns(ColourPawn.Blue));
    }
}

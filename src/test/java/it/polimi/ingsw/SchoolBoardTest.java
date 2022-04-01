package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolBoardTest {

    @Test
    public void testSchoolBoard(){
        SchoolBoard sb = new SchoolBoard();
        assertEquals(8, sb.getSpareTowers());
    }

    @Test
    public void testAddTower(){
        SchoolBoard sb = new SchoolBoard();
        sb.addTower();
        assertEquals(9, sb.getSpareTowers());
    }

    @Test
    public void testRemoveTower(){
        SchoolBoard sb = new SchoolBoard();
        sb.removeTower();
        assertEquals(7, sb.getSpareTowers());
    }

    @Test
    public void testAddProfessor(){
        SchoolBoard sb = new SchoolBoard();
        assertEquals(true,  sb.getProfessors().equals(new PawnsMap()));
        sb.addProfessor(ColourPawn.Yellow);
        assertEquals(false,  sb.getProfessors().equals(new PawnsMap()));
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow);
        assertEquals(true,  sb.getProfessors().equals(map));
    }

    @Test
    public void testRemoveProfessor(){
        SchoolBoard sb = new SchoolBoard();
        assertEquals(true,  sb.getProfessors().equals(new PawnsMap()));
        sb.addProfessor(ColourPawn.Yellow);
        assertEquals(false,  sb.getProfessors().equals(new PawnsMap()));
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow);
        assertEquals(true,  sb.getProfessors().equals(map));
        sb.removeProfessor(ColourPawn.Yellow);
        assertEquals(true,  sb.getProfessors().equals(new PawnsMap()));
    }

    @Test
    public void testAddToDiningRoom(){
        SchoolBoard sb = new SchoolBoard();
        assertEquals(true, sb.getDiningRoom().isEmpty());
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 2);
        map.add(ColourPawn.Red, 8);
        assertEquals(6, sb.addToDiningRoom(map));
        map = new PawnsMap();
        map.add(ColourPawn.Pink);
        assertEquals(1, sb.addToDiningRoom(map));
    }

    @Test
    public void testAddToEntrance(){
        SchoolBoard sb = new SchoolBoard();
        assertEquals(true, sb.getDiningRoom().isEmpty());
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 2);
        map.add(ColourPawn.Red, 3);
        sb.addToEntrance(map);
        assertEquals(true, sb.getEntrance().equals(map));
        map = new PawnsMap();
        map.add(ColourPawn.Pink);
        assertEquals(false, sb.getEntrance().equals(map));
    }

    @Test
    public void testInsertCloud(){
        SchoolBoard sb = new SchoolBoard();
        assertEquals(true, sb.getEntrance().isEmpty());
        Cloud cl = new Cloud(1);
        assertEquals(true, cl.getStudents().isEmpty());

        cl.addStudent(ColourPawn.Yellow);
        cl.addStudent(ColourPawn.Red);
        assertEquals(false, cl.getStudents().isEmpty());

        sb.insertCloud(cl);
        assertEquals(false, sb.getEntrance().isEmpty());
        assertEquals(true, cl.getStudents().isEmpty());
    }

    @Test
    public void testFromEntranceToDiningRoom() {
        SchoolBoard sb = new SchoolBoard();
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 2);
        map.add(ColourPawn.Red, 3);
        sb.addToEntrance(map);

        PawnsMap map2 = new PawnsMap();
        map2.add(ColourPawn.Yellow, 3);
        map2.add(ColourPawn.Blue, 6);
        map2.add(ColourPawn.Green, 3);
        map2.add(ColourPawn.Pink, 2);
        map2.add(ColourPawn.Red, 3);
        sb.addToDiningRoom(map2);

        PawnsMap map3 = new PawnsMap();
        map3.add(ColourPawn.Yellow, 1);
        map3.add(ColourPawn.Blue, 2);
        sb.fromEntranceToDiningRoom(map3);
        map2.add(map3);
        assertEquals(true, sb.getDiningRoom().equals(map2));

        map.remove(map3);
        assertEquals(true, sb.getEntrance().equals(map));
    }
}
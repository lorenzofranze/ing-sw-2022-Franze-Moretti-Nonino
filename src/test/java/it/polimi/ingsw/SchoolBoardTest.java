package it.polimi.ingsw;

import it.polimi.ingsw.Model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    public void testAddToDiningRoom1(){
        //questa parte serve per usare metodo con giusta segnatura
        ArrayList<String> l1= new ArrayList<>();
        l1.add("lorenzo");
        l1.add("lara");
        Game g = new Game(l1, 2);

        // test solo dell'inserimento degli studenti (lara)
        SchoolBoard sb = g.getPlayers().get(0).getSchoolBoard();
        assertEquals(true, sb.getDiningRoom().isEmpty());
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 2);
        map.add(ColourPawn.Red, 8);
        assertEquals(6, sb.addToDiningRoom(map, g));
        map = new PawnsMap();
        map.add(ColourPawn.Pink);
        assertEquals(1, sb.addToDiningRoom(map, g));

        // test metodo privato verifyProfessorInfluence

        assertTrue(g.getProfessorsLeft().isEmpty());
        PawnsMap map1 = new PawnsMap();
        SchoolBoard sb1 = g.getPlayers().get(1).getSchoolBoard();
        map1.add(ColourPawn.Yellow, 3);
        map1.add(ColourPawn.Blue, 8);
        map1.add(ColourPawn.Green, 4);
        map1.add(ColourPawn.Pink, 2);
        map1.add(ColourPawn.Red, 8);
        sb1.addToDiningRoom(map1, g);

        // verify: first player has yellow, pink and red professsors, second player has blue and green
        assertTrue(sb.getProfessors().get(ColourPawn.Yellow)==1 && sb.getProfessors().get(ColourPawn.Pink)==1 &&
                sb.getProfessors().get(ColourPawn.Red)==1 && sb1.getProfessors().get(ColourPawn.Blue)==1 &&
                sb1.getProfessors().get(ColourPawn.Green)==1 && sb.getProfessors().pawnsNumber()==3 &&
                sb1.getProfessors().pawnsNumber() == 2);



    }

    // verify position of professors with 3 players
    @Test
    public void testAddToDiningRoom2(){
        //questa parte serve per usare metodo con giusta segnatura
        ArrayList<String> l1= new ArrayList<>();
        l1.add("lorenzo");
        l1.add("lara");
        l1.add("vale");
        Game g = new Game(l1, 3);


        SchoolBoard sb1 = g.getPlayers().get(0).getSchoolBoard();
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Green, 3);
        sb1.addToDiningRoom(map, g);

        PawnsMap prof = new PawnsMap(); // prof attesi in game dopo inserimento
        prof.add(ColourPawn.Blue);
        prof.add(ColourPawn.Pink);
        prof.add(ColourPawn.Red);

        PawnsMap prof2 = new PawnsMap(); // prof attesi nella school board del player dopo inserimento
        prof2.add(ColourPawn.Yellow);
        prof2.add(ColourPawn.Green);

        SchoolBoard sb2 = g.getPlayers().get(1).getSchoolBoard();
        SchoolBoard sb3 = g.getPlayers().get(2).getSchoolBoard();
        assertTrue(g.getProfessorsLeft().equals(prof) && sb1.getProfessors().equals(prof2) && sb2.getProfessors().isEmpty()
        && sb3.getProfessors().isEmpty());

        PawnsMap map1 = new PawnsMap();

        map1.add(ColourPawn.Yellow, 3); // same number as first player
        map1.add(ColourPawn.Green, 4); // a pawn more than firs player
        map1.add(ColourPawn.Pink, 2); // colour left in game reserve
        sb2.addToDiningRoom(map1, g);

        prof.remove(ColourPawn.Pink);
        prof2.remove(ColourPawn.Green);
        PawnsMap prof3 = new PawnsMap();
        prof3.add(ColourPawn.Pink);
        prof3.add(ColourPawn.Green);


        assertTrue(g.getProfessorsLeft().equals(prof) && sb1.getProfessors().equals(prof2)
                &&  sb2.getProfessors().equals(prof3) && sb3.getProfessors().isEmpty());

        PawnsMap map2 = new PawnsMap();
        map2.add(ColourPawn.Yellow, 4); // take from first player
        map2.add(ColourPawn.Green, 5); // take from second player
        sb3.addToDiningRoom(map2, g);

        PawnsMap prof4 = new PawnsMap();
        prof4.add(ColourPawn.Yellow);
        prof4.add(ColourPawn.Green);

        prof2.remove(ColourPawn.Yellow);
        prof3.remove(ColourPawn.Green);
        assertTrue(g.getProfessorsLeft().equals(prof) && sb1.getProfessors().equals(prof2)
                &&  sb2.getProfessors().equals(prof3) && sb3.getProfessors().equals(prof4));

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
        sb.getEntrance().add(map);
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

        cl.getStudents().add(ColourPawn.Yellow);
        cl.getStudents().add(ColourPawn.Red);
        assertEquals(false, cl.getStudents().isEmpty());

        sb.insertCloud(cl);
        assertEquals(false, sb.getEntrance().isEmpty());
        assertEquals(true, cl.getStudents().isEmpty());
    }

    @Test
    public void testFromEntranceToDiningRoom() {
        //questa parte solo per usare metodo con giusta segnatura
        ArrayList<String> l1= new ArrayList<>();
        l1.add("lorenzo");
        l1.add("lara");
        Game g = new Game(l1, 2);


        SchoolBoard sb = g.getPlayers().get(0).getSchoolBoard();
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 2);
        map.add(ColourPawn.Red, 3);
        sb.getEntrance().add(map);

        PawnsMap map2 = new PawnsMap();
        map2.add(ColourPawn.Yellow, 3);
        map2.add(ColourPawn.Blue, 6);
        map2.add(ColourPawn.Green, 3);
        map2.add(ColourPawn.Pink, 2);
        map2.add(ColourPawn.Red, 3);
        sb.addToDiningRoom(map2, g);

        PawnsMap map3 = new PawnsMap();
        map3.add(ColourPawn.Yellow, 1);
        map3.add(ColourPawn.Blue, 2);
        sb.fromEntranceToDiningRoom(map3, g);
        map2.add(map3);
        assertEquals(true, sb.getDiningRoom().equals(map2));

        map.remove(map3);
        assertEquals(true, sb.getEntrance().equals(map));

    }
}
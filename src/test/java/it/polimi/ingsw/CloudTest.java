package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    @Test
    public void testCloud(){
        Cloud cloud=new Cloud(3);
        assertEquals(3, cloud.getCloudId());
        for (ColourPawn c: ColourPawn.values()) {
            assertEquals(0, cloud.getStudents().getPawns(c));
        }
    }

    @Test
    public void testAddStudent() {
        Cloud cloud = new Cloud(3);
        ColourPawn student = ColourPawn.Blue;
        PawnsMap oldStudents = new PawnsMap();

        for (ColourPawn c : ColourPawn.values()) {
            oldStudents.addPawns(c,cloud.getStudents().getPawns(c));
        }
        cloud.addStudent(student);
        for (ColourPawn c : ColourPawn.values()) {
            if (c == ColourPawn.Blue) {
                assertEquals(cloud.getStudents().getPawns(c),oldStudents.getPawns(c) + 1);
            } else {
                assertEquals(cloud.getStudents().getPawns(c), oldStudents.getPawns(c));
            }
        }
    }

    @Test
    public void testClearCloud(){
        Cloud cloud = new Cloud(3);
        //il test valuta la rimozione di 4 studenti: ma il numero di stdenti sulla nuvola varia
        // in base al numero di players
        for (ColourPawn c : ColourPawn.values()) {
            cloud.addStudent(c);
        }
        PawnsMap returnClearCloud=cloud.clearCloud();
        PawnsMap oldStudents=new PawnsMap();
        for (ColourPawn c : ColourPawn.values()) {
            oldStudents.addPawns(c,returnClearCloud.getPawns(c));
        }
        for (ColourPawn c : ColourPawn.values()) {
            assertEquals(1,oldStudents.getPawns(c));
            assertEquals(0,cloud.getStudents().getPawns(c));
        }
    }
}
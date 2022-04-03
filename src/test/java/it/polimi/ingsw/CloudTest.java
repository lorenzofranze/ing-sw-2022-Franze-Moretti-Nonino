package it.polimi.ingsw;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.ColourPawn;
import it.polimi.ingsw.Model.PawnsMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    @Test
    public void testCloud(){
        Cloud cloud=new Cloud(3);
        assertEquals(3, cloud.getCloudId());
        for (ColourPawn c: ColourPawn.values()) {
            assertEquals(0, cloud.getStudents().get(c));
        }
    }

    @Test
    public void testAddStudent() {
        Cloud cloud = new Cloud(3);
        ColourPawn student = ColourPawn.Blue;
        PawnsMap oldStudents = new PawnsMap();

        for (ColourPawn c : ColourPawn.values()) {
            oldStudents.add(c,cloud.getStudents().get(c));
        }
        cloud.addStudent(student);
        for (ColourPawn c : ColourPawn.values()) {
            if (c == ColourPawn.Blue) {
                assertEquals(cloud.getStudents().get(c),oldStudents.get(c) + 1);
            } else {
                assertEquals(cloud.getStudents().get(c), oldStudents.get(c));
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
            oldStudents.add(c,returnClearCloud.get(c));
        }
        for (ColourPawn c : ColourPawn.values()) {
            assertEquals(1,oldStudents.get(c));
            assertEquals(0,cloud.getStudents().get(c));
        }
    }
}
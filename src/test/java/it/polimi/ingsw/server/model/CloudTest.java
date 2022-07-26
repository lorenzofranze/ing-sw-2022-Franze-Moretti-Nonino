package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.PawnsMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
        cloud.getStudents().add(student);
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
            cloud.getStudents().add(c);
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

    @Test
    public void testToPojo(){
        Cloud cloud = new Cloud(3);

        for (ColourPawn c : ColourPawn.values()) {
            cloud.getStudents().add(c);
        }

        it.polimi.ingsw.common.gamePojo.CloudPojo pojoCloud = cloud.toPojo();

        Map pojoMap = pojoCloud.getStudents().getPawns();
        Map map = cloud.getStudents().getPawns();

        assertEquals(true, map.equals(pojoMap));
    }
}
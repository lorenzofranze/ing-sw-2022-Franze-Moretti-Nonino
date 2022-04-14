package it.polimi.ingsw;

import it.polimi.ingsw.server.Model.ColourPawn;
import it.polimi.ingsw.server.Model.PawnsMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PawnsMapTest {
    @Test
    public void testPawnsMap(){
        PawnsMap map=new PawnsMap();
        assertEquals(0, map.pawnsNumber());
    }

    @Test
    public void testAdd(){
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        assertEquals(3, map.get(ColourPawn.Yellow));
        map.add(ColourPawn.Yellow);
        assertEquals(4, map.get(ColourPawn.Yellow));

        PawnsMap map2 = new PawnsMap();
        map2.add(ColourPawn.Yellow, 3);
        map2.add(map);
        assertEquals(7, map2.get(ColourPawn.Yellow));

        map2.add(ColourPawn.Blue);
        map.add(map2);
        assertEquals(1, map2.get(ColourPawn.Blue));
    }

    @Test
    public void testRemove(){
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 1);

        map.remove(ColourPawn.Yellow);
        assertEquals(2, map.get(ColourPawn.Yellow));

        map.remove(ColourPawn.Yellow, 2);
        assertEquals(0, map.get(ColourPawn.Yellow));

        PawnsMap map2 = new PawnsMap();
        map2.add(ColourPawn.Yellow, 3);
        map2.add(ColourPawn.Blue, 7);
        map2.add(ColourPawn.Pink, 3);
        map.add(ColourPawn.Yellow, 2);
        map.add(ColourPawn.Blue, 2);
        map2.remove(map);
        assertEquals(1, map2.get(ColourPawn.Yellow));
        assertEquals(4, map2.get(ColourPawn.Blue));
        assertEquals(3, map2.get(ColourPawn.Pink));
    }

    @Test
    public void testRemoveRandomly(){
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 3);
        map.add(ColourPawn.Red, 8);
        PawnsMap copy = map.clone();
        assertEquals(23, map.pawnsNumber());

        PawnsMap removed = map.removeRandomly(5);

        assertEquals(18, map.pawnsNumber());
        assertEquals(5, removed.pawnsNumber());

        map.add(removed);
        assertEquals(true, map.equals(copy));

        map = copy.clone();
        assertEquals(23, map.pawnsNumber());
        ColourPawn deleted = map.removeRandomly();
        assertEquals(22, map.pawnsNumber());
        map.add(deleted);
        assertEquals(true, map.equals(copy));
    }

    @Test
    public void testIsEmpty(){
        PawnsMap map = new PawnsMap();
        assertEquals(true, map.isEmpty());
        map.add(ColourPawn.Yellow);
        assertEquals(false, map.isEmpty());
    }

    @Test
    public void testPawnsNumber(){
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 3);
        map.add(ColourPawn.Red, 8);
        assertEquals(23, map.pawnsNumber());
        map.remove(ColourPawn.Yellow,2);
        assertEquals(21, map.pawnsNumber());
    }

    @Test
    public void testClone(){
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 3);
        map.add(ColourPawn.Red, 8);
        PawnsMap map2 = map.clone();
        assertEquals(true, map.equals(map2));
    }

    @Test
    public void testEquals(){
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 3);
        map.add(ColourPawn.Red, 8);
        PawnsMap map2 = map.clone();
        assertEquals(true, map.equals(map2));
        map2.add(ColourPawn.Yellow);
        assertEquals(false, map.equals(map2));
    }

    @Test
    public String toString() {
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 3);
        map.add(ColourPawn.Red, 8);
        String prova = map.toString();
        String[] lines = prova.split("\n");

        assertEquals(5, lines.length);

        return prova;
    }




}
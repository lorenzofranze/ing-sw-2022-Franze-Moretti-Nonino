package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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





}
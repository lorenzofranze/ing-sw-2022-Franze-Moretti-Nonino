package it.polimi.ingsw;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.server.model.PawnsMap;
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

        PawnsMap map3 = new PawnsMap();
        map3.add(ColourPawn.Yellow, 3);
        map3.add(ColourPawn.Blue, 6);
        map3.add(ColourPawn.Green, 3);
        map3.add(ColourPawn.Pink, 3);
        map3.add(ColourPawn.Red, 8);

        map3.remove(ColourPawn.Yellow, 2);

        assertEquals(1, map3.get(ColourPawn.Yellow));
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

        PawnsMap map3 = new PawnsMap();
        map3.add(ColourPawn.Yellow, 3);
        map3.add(ColourPawn.Blue, 6);
        map3.add(ColourPawn.Green, 3);
        map3.add(ColourPawn.Pink, 3);
        map3.add(ColourPawn.Red, 8);
        int pre = map3.pawnsNumber();
        PawnsMap map4 = map3.removeRandomly(2);

        assertEquals(true, map4.pawnsNumber()+map3.pawnsNumber()==pre);

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
    public void testToString() {
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 3);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Green, 3);
        map.add(ColourPawn.Pink, 3);
        map.add(ColourPawn.Red, 8);
        String prova = map.toString();

        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PINK = "\u001B[35m";

         String[] ansi_colours = {ANSI_YELLOW, ANSI_BLUE, ANSI_GREEN, ANSI_RED, ANSI_PINK, ANSI_RESET};

        String ris = new String();
        int i = 0;
        for (ColourPawn p : ColourPawn.values()) {
            ris = ris + ("("+ansi_colours[i]+p.toString() + " , " + map.getPawns().get(p) + ansi_colours[5]+") ");
            i++;
        }

        assertEquals(true, ris.equals(prova));
    }




}
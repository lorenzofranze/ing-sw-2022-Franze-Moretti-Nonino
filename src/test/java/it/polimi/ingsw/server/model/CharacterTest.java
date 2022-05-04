package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CharacterPojo;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @Test
    public void testUse() {
        Character c = new Character(1, 2);
        assertEquals(2, c.getCost());
        c.use();
        assertEquals(3, c.getCost());
    }

    @Test
    public void testToPojo() {
        Character c = new Character(1, 2);
        CharacterPojo pojo = c.toPojo();
        assertEquals(true, c.getCost()==pojo.getActualCost());
        assertEquals(true, c.isIncremented()== pojo.isIncremented());
        c.use();
        pojo = c.toPojo();
        assertEquals(true, c.isIncremented()== pojo.isIncremented());
    }

    @Test
    public void testEquals() {
        Character c1 = new Character(1, 2);
        Character c2 = new Character(1, 2);
        assertEquals(true, c1.equals(c2));
        Character c3 = new Character(3, 2);
        assertEquals(false, c1.equals(c3));
    }

    @Test
    public void testHashCode() {
        Character c1 = new Character(1, 2);
        int h = c1.hashCode();
        assertEquals(h, Objects.hash(c1.getCharacterId()));

    }


}
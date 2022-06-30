package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CharacterPojo;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CharacterStateTest {

    @Test
    public void testUse() {
        CharacterState c = new CharacterState(1, 2);
        assertEquals(2, c.getCost());
        c.use();
        assertEquals(3, c.getCost());
    }

    @Test
    public void testToPojo() {
        CharacterState c = new CharacterState(1, 2);
        CharacterPojo pojo = c.toPojo();
        assertEquals(true, c.getCost()==pojo.getActualCost());
        assertEquals(true, c.isIncremented()== pojo.isIncremented());
        c.use();
        pojo = c.toPojo();
        assertEquals(true, c.isIncremented()== pojo.isIncremented());
    }

    @Test
    public void testEquals() {
        CharacterState c1 = new CharacterState(1, 2);
        CharacterState c2 = new CharacterState(1, 2);
        assertEquals(true, c1.equals(c2));
        CharacterState c3 = new CharacterState(3, 2);
        assertEquals(false, c1.equals(c3));
    }

    @Test
    public void testHashCode() {
        CharacterState c1 = new CharacterState(1, 2);
        int h = c1.hashCode();
        assertEquals(h, Objects.hash(c1.getCharacterId()));

    }

    @Test
    public void testSetIncremented() {
        CharacterState c1 = new CharacterState(1, 2);
        c1.setIncremented(true);
        assertEquals(true, c1.isIncremented());
        c1.setIncremented(false);
        assertEquals(false, c1.isIncremented());

    }


}
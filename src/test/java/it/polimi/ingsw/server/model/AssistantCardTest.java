package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.AssistantCardPojo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {

    @Test
    public void testEquals() {
        AssistantCard c1 = new AssistantCard(1,3);
        AssistantCard c2 = new AssistantCard(1, 3);
        AssistantCard c3 = new AssistantCard(2,4);
        assertEquals(false, c1.equals(c3));
        assertEquals(true, c1.equals(c2));
        assertEquals(1, c1.getTurnOrder());
        assertEquals(3, c1.getMovementsMotherNature());
    }


    @Test
    public void testToString() {
        AssistantCard c1 = new AssistantCard(1,3);
        String s = "AssistantCard{turnOrder=1, movementsMotherNature=3}";
        assertEquals(true, c1.toString().equals(s));
    }

    @Test
    public void testToPojo() {
        AssistantCard c1 = new AssistantCard(1,3);
        AssistantCardPojo pojoCard = c1.toPojo();

        assertEquals(true, c1.getMovementsMotherNature()==pojoCard.getMovementsMotherNature());
        assertEquals(true, c1.getTurnOrder()==pojoCard.getTurnOrder());
    }

}
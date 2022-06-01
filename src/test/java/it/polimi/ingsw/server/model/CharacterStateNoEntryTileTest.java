package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CharacterPojo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterStateNoEntryTileTest {

    @Test
    public void addNoEntryTile() {
        CharacterStateNoEntryTile card = new CharacterStateNoEntryTile(3,2);
        card.addNoEntryTile();
        assertEquals(1, card.getNumNoEntry());
        card.addNoEntryTile();
        assertEquals(2, card.getNumNoEntry());
    }

    @Test
    public void removeNoEntryTile() {
        CharacterStateNoEntryTile card = new CharacterStateNoEntryTile(3,2);
        card.addNoEntryTile();
        assertEquals(1, card.getNumNoEntry());
        card.addNoEntryTile();
        assertEquals(2, card.getNumNoEntry());
        card.removeNoEntryTile();
        assertEquals(1, card.getNumNoEntry());
        card.removeNoEntryTile();
        assertEquals(0, card.getNumNoEntry());
    }

    @Test
    public void testToPojo() {
        CharacterStateNoEntryTile card = new CharacterStateNoEntryTile(3,2);
        card.addNoEntryTile();
        CharacterPojo pojo = card.toPojo();
        assertEquals(3, pojo.getCharacterId());
        assertEquals(2, pojo.getActualCost());
        assertEquals(1, pojo.getNumNoEntry());
        assertEquals(false, pojo.isIncremented());
    }

}
package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.AssistantCardPojo;
import it.polimi.ingsw.common.gamePojo.ColourTower;
import it.polimi.ingsw.common.gamePojo.ColourWizard;
import it.polimi.ingsw.common.gamePojo.PlayerPojo;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    @Test
    public void testPlayAssistantCard() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);

        // remove card2
        p1.playAssistantCard(2);
        Set deck = new HashSet();
        AssistantCard card1 = new AssistantCard(1, 1);
        AssistantCard card2 = new AssistantCard(2, 1);
        AssistantCard card3 = new AssistantCard(3, 2);
        AssistantCard card4 = new AssistantCard(4, 2);
        AssistantCard card5 = new AssistantCard(5, 3);
        AssistantCard card6 = new AssistantCard(6, 3);
        AssistantCard card7 = new AssistantCard(7, 4);
        AssistantCard card8 = new AssistantCard(8, 4);
        AssistantCard card9 = new AssistantCard(9, 5);
        AssistantCard card10 = new AssistantCard(10, 5);
        deck.add(card1);
        deck.add(card3);
        deck.add(card4);
        deck.add(card5);
        deck.add(card6);
        deck.add(card7);
        deck.add(card8);
        deck.add(card9);
        deck.add(card10);
        assertTrue(deck.equals(p1.getDeck()));
        assertTrue(card2.equals(p1.getPlayedAssistantCard()));

        // remove card2 again
        p1.playAssistantCard(2);
        assertTrue(deck.equals(p1.getDeck()));
        assertTrue(card2.equals(p1.getPlayedAssistantCard()));
    }

    @Test
    public void testResetAssistantCard() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);

        // remove card2
        p1.playAssistantCard(2);
        Set deck = new HashSet();
        AssistantCard card1 = new AssistantCard(1, 1);
        AssistantCard card2 = new AssistantCard(2, 1);
        AssistantCard card3 = new AssistantCard(3, 2);
        AssistantCard card4 = new AssistantCard(4, 2);
        AssistantCard card5 = new AssistantCard(5, 3);
        AssistantCard card6 = new AssistantCard(6, 3);
        AssistantCard card7 = new AssistantCard(7, 4);
        AssistantCard card8 = new AssistantCard(8, 4);
        AssistantCard card9 = new AssistantCard(9, 5);
        AssistantCard card10 = new AssistantCard(10, 5);
        deck.add(card1);
        deck.add(card3);
        deck.add(card4);
        deck.add(card5);
        deck.add(card6);
        deck.add(card7);
        deck.add(card8);
        deck.add(card9);
        deck.add(card10);
        assertTrue(deck.equals(p1.getDeck()));
        assertTrue(card2.equals(p1.getPlayedAssistantCard()));

        // remove card2 again
        p1.playAssistantCard(2);
        assertTrue(deck.equals(p1.getDeck()));
        assertTrue(card2.equals(p1.getPlayedAssistantCard()));

        p1.resetAssistantCard();
        assertTrue(p1.getPlayedAssistantCard() == null);

        p1.setPlayedAssistantCard(card2);
        p1.resetAssistantCard();
        assertTrue(p1.getPlayedAssistantCard() == null);
    }

    @Test
    public void testAddCoins() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);
        int i = p1.getCoins();
        p1.addCoins(3);
        assertEquals(true, p1.getCoins() == 3 + i);
    }

    @Test
    public void testRemoveCoins() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);
        int i = p1.getCoins();
        p1.addCoins(3);
        assertEquals(true, p1.getCoins() == 3 + i);
        i = p1.getCoins();
        p1.removeCoins(2);
        assertEquals(true, p1.getCoins() == i - 2);
    }

    @Test
    public void testToString() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);
        assertEquals(true, p1.getNickname().equals(p1.toString()));
    }

    @Test
    public void testIsConnected() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);
        p1.setConnected(true);
        assertEquals(true, p1.isConnected());
    }

    @Test
    public void testToPojo() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);
        p1.playAssistantCard(1);
        PlayerPojo pojoPlayer = p1.toPojo();

        assertEquals(true, pojoPlayer.getNickname().equals(p1.getNickname()));
        assertEquals(true, pojoPlayer.getCoins() == p1.getCoins());
        assertEquals(true, pojoPlayer.getColourTower().equals(p1.getColourTower()));
        assertEquals(true, pojoPlayer.getWizard().equals(p1.getWizard()));
        assertEquals(true, pojoPlayer.getSchoolBoard().getDiningRoom().getPawns().equals(p1.getSchoolBoard().getDiningRoom().getPawns()));
        assertEquals(true, pojoPlayer.getSchoolBoard().getEntrance().getPawns().equals(p1.getSchoolBoard().getEntrance().getPawns()));
        assertEquals(true, pojoPlayer.getSchoolBoard().getProfessors().getPawns().equals(p1.getSchoolBoard().getProfessors().getPawns()));
        assertEquals(true, pojoPlayer.getSchoolBoard().getSpareTowers() == p1.getSchoolBoard().getSpareTowers());

        Set<AssistantCardPojo> deck  = new HashSet<>();
        for (AssistantCard a : p1.getDeck()){
            AssistantCardPojo pojoAssistantCardPojo = a.toPojo();
            deck.add(pojoAssistantCardPojo);
        }
        pojoPlayer.setDeck(deck);

        assertEquals(true, pojoPlayer.getDeck().equals(deck));
        AssistantCardPojo pojo = p1.getPlayedAssistantCard().toPojo();
        pojoPlayer.setPlayedAssistantCard(pojo);
        assertEquals(true, pojoPlayer.getPlayedAssistantCard().equals(pojo));
    }

    @Test
    public void testHashCode() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);
        int h = p1.hashCode();
        assertEquals(h, Objects.hash(p1.getNickname()));
    }

    @Test
    public void testEquals(){
        Player p1 = new Player("Lara", ColourTower.Black, ColourWizard.Violet);
        Player p2 = new Player("Vale", ColourTower.Grey, ColourWizard.Green);
        Player p3 = new Player("Lara", ColourTower.Black, ColourWizard.Violet);
        assertEquals(false, p1.equals(p2));
        assertEquals(true, p1.equals(p3));

    }
}
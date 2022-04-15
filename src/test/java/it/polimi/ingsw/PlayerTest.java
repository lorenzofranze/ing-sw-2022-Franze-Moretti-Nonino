package it.polimi.ingsw;

import it.polimi.ingsw.Server.Model.AssistantCard;
import it.polimi.ingsw.Server.Model.ColourTower;
import it.polimi.ingsw.Server.Model.ColourWizard;
import it.polimi.ingsw.Server.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    @Test
    void playAssistantCard() {
        Player p1 = new Player("pippo", ColourTower.Black, ColourWizard.Green);

        // remove card2
        p1.playAssistantCard(2);
        Set deck = new HashSet();
                AssistantCard card1 = new AssistantCard(1,1);
                AssistantCard card2 = new AssistantCard(2,1);
                AssistantCard card3 = new AssistantCard(3,2);
                AssistantCard card4 = new AssistantCard(4,2);
                AssistantCard card5 = new AssistantCard(5,3);
                AssistantCard card6 = new AssistantCard(6,3);
                AssistantCard card7 = new AssistantCard(7,4);
                AssistantCard card8 = new AssistantCard(8,4);
                AssistantCard card9 = new AssistantCard(9,5);
                AssistantCard card10 = new AssistantCard(10,5);
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
}
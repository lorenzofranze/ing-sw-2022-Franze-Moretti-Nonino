package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    @Test
    public void testSetCurrentPlayer(){
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, false);
        game.setCurrentPlayer(player);
        assertEquals(player, game.getCurrentPlayer());
    }

    @Test
    public void testSetGameId(){
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, false);
        game.setGameId("12");
        assertEquals("12", game.getGameId());
    }
    @Test
    public void testAddCoinsComplexModeOn() {
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, true);
        int numCoins= game.getCoinSupply()+1;
        int num=6;
        game.addCoins(num);
        assertEquals(numCoins, game.getCoinSupply());
    }
    @Test
    public void testAddCoinsComplexModeOff() {
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, false);
        int num=6;
        game.addCoins(num);
        assertEquals(0, game.getCoinSupply());
    }
    @Test
    public void testRemoveCoinsComplexModeOn() {
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, true);
        int num=game.getCoinSupply();
        game.removeCoins(num);
        assertEquals(0, game.getCoinSupply());
    }
    @Test
    public void testRemoveCoinsComplexModeOff() {
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, false);
        int num= game.getCoinSupply();
        game.removeCoins(num);
        assertEquals(0, game.getCoinSupply());
    }


    public void setActiveEffectComplexModeOn(){
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, false);
        Character character=game.getCharacters().get(1);
        game.setActiveEffect(character);
        assertEquals(character, game.getActiveEffect());
    }


}
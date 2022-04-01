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
        list.add(player);
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
        int numCoins= game.getCoinSupply();
        int num=6;
        game.addCoins(num);
        assertEquals(numCoins+6, game.getCoinSupply());
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

    @Test
    public void setActiveEffectComplexModeOn(){
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, false);
        Character character=game.getCharacters().get(1);
        game.setActiveEffect(character);
        assertEquals(character, game.getActiveEffect());
    }


    @Test
    public void testUnifyIsland(){
        List<Player> list= new ArrayList<>();
        Player player = new Player("testPlayer", ColourTower.Black, ColourWizard.Blue);
        Game game= new Game(list, false);

        Island island1=game.getIslands().get(2);
        Island island2=game.getIslands().get(3);

        island1.setTowerColor(ColourTower.Grey);
        island1.addTower(5);
        island1.addStudents(ColourPawn.Yellow,3);
        island2.setTowerColor(ColourTower.Grey);
        island2.addTower(2);
        island2.addStudents(ColourPawn.Blue,1);
        island2.addStudents(ColourPawn.Yellow,2);
        ArrayList<Island> islandList= new ArrayList<Island>();
        islandList.add(island1);
        islandList.add(island2);
        game.unifyIslands(islandList);

        assertEquals(5,game.getIslands().get(2).getStudents().get(ColourPawn.Yellow));
        assertEquals(1,game.getIslands().get(2).getStudents().get(ColourPawn.Blue));
        assertEquals(7, game.getIslands().get(2).getTowerCount());
    }
}
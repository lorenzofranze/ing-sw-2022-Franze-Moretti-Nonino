package it.polimi.ingsw;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Character;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    @Test
    public void testSetCurrentPlayer(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);

        Game game=new Game(listPlayer, listTower, listWizard,false);
        game.setCurrentPlayer(game.getPlayers().get(0));
        assertEquals(game.getPlayers().get(0), game.getCurrentPlayer());
    }

    @Test
    public void testSetGameId(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);
        Game game=new Game(listPlayer, listTower, listWizard,false);
        game.setGameId("12");
        assertEquals("12", game.getGameId());
    }
    @Test
    public void testAddCoinsComplexModeOn() {
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);
        Game game=new Game(listPlayer, listTower, listWizard,true);
        int numCoins= game.getCoinSupply();
        int num=6;
        game.addCoins(num);
        assertEquals(numCoins+6, game.getCoinSupply());
    }

    @Test
    public void testRemoveCoinsComplexModeOn() {
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);
        Game game=new Game(listPlayer, listTower, listWizard,true);
        int num=game.getCoinSupply();
        game.removeCoins(num);
        assertEquals(0, game.getCoinSupply());
    }
    @Test
    public void testRemoveCoinsComplexModeOff() {
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);
        Game game=new Game(listPlayer, listTower, listWizard,false);
        int num= game.getCoinSupply();
        game.removeCoins(num);
        assertEquals(0, game.getCoinSupply());
    }

    @Test
    public void setActiveEffectComplexModeOn(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);
        Game game=new Game(listPlayer, listTower, listWizard,true);
        Character character=game.getCharacters().get(1);
        game.setActiveEffect(character);
        assertEquals(character, game.getActiveEffect());
    }


    @Test
    public void testUnifyIsland(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);
        Game game=new Game(listPlayer, listTower, listWizard,false);

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
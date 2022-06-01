package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.ColourTower;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {
    /*
    @Test
    public void testSetCurrentPlayer(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        Game game=new Game(listPlayer, 2);
        game.setCurrentPlayer(game.getPlayers().get(0));
        assertEquals(game.getPlayers().get(0), game.getCurrentPlayer());
    }

     */

    @Test
    public void testGetStudentsBag(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        Game game=new Game(listPlayer, 2);
        PawnsMap pawnsMap= new PawnsMap();
        for (ColourPawn currColor: ColourPawn.values()){
            pawnsMap.add(currColor,26);
        }
        assertEquals(pawnsMap,game.getStudentsBag());
    }

    @Test
    public void testGetCharacterStateByID(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        Game game=new Game(listPlayer, 2);

        for(CharacterState characterState : game.getCharacters()) {
            assertEquals(characterState, game.getCharacterStateByID(characterState.getCharacterId()));
        }
    }

    @Test
    public void testSetGameId(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        Game game=new Game(listPlayer, 2);

        assertEquals(2, game.getGameId());
    }

    @Test
    public void testAddCoinsComplexModeOn() {
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        Game game=new Game(listPlayer, 2);
        game.setGameId(3);
        assertEquals(3, game.getGameId());
        game.setCoinSupply(3);
        int numCoins= game.getCoinSupply();
        assertEquals(3, numCoins);
        int num=6;
        game.addCoins(num);
        assertEquals(numCoins+6, game.getCoinSupply());

    }

    @Test
    public void testRemoveCoinsComplexModeOn() {
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Lara");
        Game game=new Game(listPlayer, 2);
        int num=game.getCoinSupply();
        game.removeCoins(num);
        assertEquals(0, game.getCoinSupply());
    }
    @Test
    public void testRemoveCoinsComplexModeOff() {
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Lara");
        Game game=new Game(listPlayer, 2);
        int num= game.getCoinSupply();
        game.removeCoins(num);
        assertEquals(0, game.getCoinSupply());
    }

    @Test
    public void setActiveEffectComplexModeOn(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");

        Game game=new Game(listPlayer, 2);
        CharacterState characterState =game.getCharacters().get(1);
        game.setActiveEffect(characterState);
        assertEquals(characterState, game.getActiveEffect());
    }


    @Test
    public void testUnifyIsland(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");

        Game game=new Game(listPlayer, 2);

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

    @Test
    public void testFindMotherNature(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        Game game=new Game(listPlayer, 2);
        game.getIslands().get(3).setHasMotherNature(true);
        int found = game.findMotherNature();
        for(int i = 0; i< game.getIslands().size(); i++){
            if (i!=found){
                assertEquals(false, game.getIslands().get(i).getHasMotherNature());
            } else{
                assertEquals(true, game.getIslands().get(i).getHasMotherNature());
            }
        }
    }

    @Test
    public void testGetIslandOfIndex(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        Game game=new Game(listPlayer, 2);
        Island i = game.getIslandOfIndex(3);
        assertEquals(true, game.getIslands().get(3).equals(i));
    }

    @Test
    public void testIslandsToString(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        listPlayer.add("Lara");
        Game game=new Game(listPlayer, 2);
        game.getIslands().get(3).setTowerColor(ColourTower.Grey);
        String islands = game.islandsToString();
        String[] lines = islands.split("\n");
        assertEquals(12, lines.length);
    }

    @Test
    public void testCloudsToString(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        listPlayer.add("Lara");
        Game game=new Game(listPlayer, 2);
        String s = game.cloudsToString();
        String[] lines = s.split("\n");
        assertEquals(3, lines.length);

        listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        game=new Game(listPlayer, 2);
        game.getClouds().get(0).getStudents().add(ColourPawn.Yellow, 4);
        s = game.cloudsToString();
        lines = s.split("\n");
        assertEquals(2, lines.length);
    }

    @Test
    public void testReassignProfessors(){
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Franzo");
        listPlayer.add("Lara");
        Game game=new Game(listPlayer, 2);
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow);
        map.add(ColourPawn.Blue);
        map.add(ColourPawn.Pink);
        map.add(ColourPawn.Red);
        map.add(ColourPawn.Green);
        game.setProfessorsLeft(map);

        for(int i =  0; i<game.getPlayers().size(); i++){
            assertEquals(0, game.getPlayers().get(i).getSchoolBoard().getProfessors().pawnsNumber());
        }

        Player p = game.getPlayers().get(0);
        assertEquals(0, p.getSchoolBoard().getDiningRoom().get(ColourPawn.Yellow));
        p.getSchoolBoard().getDiningRoom().add(ColourPawn.Yellow, 3);
        assertEquals(3, p.getSchoolBoard().getDiningRoom().get(ColourPawn.Yellow));
        assertEquals(0, p.getSchoolBoard().getProfessors().pawnsNumber());

        game.reassignProfessors();

        for(int i =  1; i<game.getPlayers().size(); i++){
            assertEquals(0, game.getPlayers().get(i).getSchoolBoard().getProfessors().pawnsNumber());
        }

        assertEquals(1, game.getPlayers().get(0).getSchoolBoard().getProfessors().pawnsNumber());
        assertEquals(4, game.getProfessorsLeft().pawnsNumber());
    }




}
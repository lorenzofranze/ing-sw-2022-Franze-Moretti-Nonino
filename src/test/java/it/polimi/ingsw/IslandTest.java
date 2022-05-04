package it.polimi.ingsw;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.ColourTower;
import it.polimi.ingsw.common.gamePojo.ColourWizard;
import it.polimi.ingsw.server.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    @Test
    public void testSetHasMotherNature() {
        Island island=new Island();
        island.setHasMotherNature(true);
        assertEquals(true, island.getHasMotherNature());
        island.setHasMotherNature(false);
        assertEquals(false, island.getHasMotherNature());
    }

    @Test
    public void testSetTowerColor() {
        Island island=new Island();
        island.setTowerColor(ColourTower.Grey);
        assertEquals(island.getTowerColour(), ColourTower.Grey);
    }
    @Test
    public void testAddTower(){
        Island island=new Island();
        island.setTowerColor(ColourTower.White);
        island.addTower(3);
        assertEquals(3,island.getTowerCount());
    }

    @Test
    public void testAddStudents() {
        Island island=new Island();
        island.getStudents().add(ColourPawn.Green, 3);
        island.getStudents().add(ColourPawn.Pink, 6);
        assertEquals(3,island.getStudents().get(ColourPawn.Green));
        assertEquals(6,island.getStudents().get(ColourPawn.Pink));
        assertEquals(0,island.getStudents().get(ColourPawn.Blue));
        assertEquals(0,island.getStudents().get(ColourPawn.Red));
        assertEquals(0,island.getStudents().get(ColourPawn.Yellow));

        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Green, 1);
        map.add(ColourPawn.Pink, 2);
        map.add(ColourPawn.Blue, 6);
        map.add(ColourPawn.Red, 2);
        map.add(ColourPawn.Yellow, 3);
        island.addStudents(map);

        assertEquals(4,island.getStudents().get(ColourPawn.Green));
        assertEquals(8,island.getStudents().get(ColourPawn.Pink));
        assertEquals(6,island.getStudents().get(ColourPawn.Blue));
        assertEquals(2,island.getStudents().get(ColourPawn.Red));
        assertEquals(3,island.getStudents().get(ColourPawn.Yellow));

        island.addStudents(ColourPawn.Blue, 1);
        assertEquals(7,island.getStudents().get(ColourPawn.Blue));

    }

    @Test
    public void testGetInfluence(){
        Island island=new Island();
        island.getStudents().add(ColourPawn.Green, 3);
        island.getStudents().add(ColourPawn.Pink, 6);
        island.getStudents().add(ColourPawn.Red, 4);
        island.getStudents().add(ColourPawn.Yellow, 2);
        island.setTowerColor(ColourTower.Grey);
        island.addTower(3);
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Lara");
        listPlayer.add("Lorenzo");
        ArrayList<ColourTower> listTower= new ArrayList<>();
        listTower.add(ColourTower.White);
        listTower.add(ColourTower.Grey);
        listTower.add(ColourTower.Black);
        ArrayList<ColourWizard> listWizard= new ArrayList<>();
        listWizard.add(ColourWizard.Blue);
        listWizard.add(ColourWizard.Green);
        listWizard.add(ColourWizard.Violet);
        Game game=new Game(listPlayer, 2);

        game.getPlayers().get(0).getSchoolBoard().getProfessors().add(ColourPawn.Yellow);
        game.getPlayers().get(1).getSchoolBoard().getProfessors().add(ColourPawn.Pink);
        game.getPlayers().get(1).getSchoolBoard().getProfessors().add(ColourPawn.Green);
        assertEquals(game.getPlayers().get(1), island.getInfluence(game));
    }

    @Test
    public void testToPojo(){
        Island island=new Island();
        island.getStudents().add(ColourPawn.Green, 3);
        island.getStudents().add(ColourPawn.Pink, 6);
        island.setTowerColor(ColourTower.White);
        island.addTower(3);
        island.setHasMotherNature(true);

        it.polimi.ingsw.common.gamePojo.IslandPojo pojoIsland = island.toPojo();

        Map pojoMap = pojoIsland.getStudents().getPawns();
        Map map = island.getStudents().getPawns();

        assertEquals(true, map.equals(pojoMap));
        assertEquals(true, (island.getTowerCount()==pojoIsland.getTowerCount()));
        assertEquals(true, island.getHasMotherNature()==pojoIsland.isHasMotherNature());
        assertEquals(true, island.getTowerColour().equals(pojoIsland.getTowerColour()));
    }

    @Test
    public void testSetNumNoEntryTile() {
        Island island=new Island();
        island.setNumNoEntryTile(3);
        assertEquals(3, island.getNumNoEntryTile());
    }

    @Test
    public void testGetOwner(){
        Island island=new Island();
        island.setTowerColor(ColourTower.Grey);
        island.addTower(3);
        ArrayList<String> listPlayer= new ArrayList<>();
        listPlayer.add("Vale");
        listPlayer.add("Lara");
        listPlayer.add("Lorenzo");
        Game game=new Game(listPlayer, 2);

        Player p = island.getOwner(game);
        assertEquals(true, p.getColourTower().equals(ColourTower.Grey));
    }


}

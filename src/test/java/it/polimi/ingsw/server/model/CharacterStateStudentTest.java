package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CharacterPojo;
import it.polimi.ingsw.common.gamePojo.ColourPawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterStateStudentTest {

    @Test
    public void addStudent() {
        CharacterStateStudent card = new CharacterStateStudent(3,2);
        card.addStudent(ColourPawn.Yellow);
        card.addStudent(ColourPawn.Yellow);
        card.addStudent(ColourPawn.Red);
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 2);
        map.add(ColourPawn.Red, 1);
        assertEquals(map, card.getAllStudents());
    }

    @Test
    public void removeStudent() {
        CharacterStateStudent card = new CharacterStateStudent(3,2);
        card.addStudent(ColourPawn.Yellow);
        card.addStudent(ColourPawn.Yellow);
        card.addStudent(ColourPawn.Red);
        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 2);
        map.add(ColourPawn.Red, 1);
        assertEquals(map, card.getAllStudents());

        card.removeStudent(ColourPawn.Yellow);
        map.remove(ColourPawn.Yellow);
        assertEquals(map, card.getAllStudents());
    }

    @Test
    public void testToPojo() {
        CharacterStateStudent card = new CharacterStateStudent(3,2);
        card.addStudent(ColourPawn.Yellow);
        card.addStudent(ColourPawn.Yellow);
        card.addStudent(ColourPawn.Red);
        CharacterPojo pojo = card.toPojo();
        assertEquals(3, pojo.getCharacterId());
        assertEquals(2, pojo.getActualCost());
        assertEquals(card.getAllStudents().getPawns(), pojo.getStudents().getPawns());
    }

    @Test
    public void testSetStudents() {
        CharacterStateStudent card = new CharacterStateStudent(3,2);

        PawnsMap map = new PawnsMap();
        map.add(ColourPawn.Yellow, 2);
        map.add(ColourPawn.Pink, 3);
        card.setStudents(map);

        assertEquals(map.getPawns(), card.getAllStudents().getPawns());
    }

}
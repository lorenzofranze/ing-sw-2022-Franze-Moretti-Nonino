package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.common.gamePojo.Character;
import it.polimi.ingsw.common.gamePojo.GameState;
import it.polimi.ingsw.common.gamePojo.*;

public class ShowGame {
    public static void show(GameState gameState){
        for(Player player : gameState.getPlayers())
            System.out.println(player.getNickname() + " : turno: " + player.getPlayedAssistantCard().getTurnOrder() + " - madre natura: " + player.getPlayedAssistantCard().getMovementsMotherNature());
        for(Player player : gameState.getPlayers()){
            System.out.println("\n\t\t\t\t" + player.getNickname() + " SCHOOLBOARD");
            System.out.println("ENTRANCE:" + player.getSchoolBoard().getEntrance());
            System.out.println("DINING ROOM:"+ player.getSchoolBoard().getDiningRoom());
            System.out.println("PROFESSORS:" + player.getSchoolBoard().getProfessors());
            System.out.println("NUM TOWERS:" + player.getSchoolBoard().getSpareTowers());
        }

        for(Island island : gameState.getIslands()){
            int i=0;
            System.out.println("ISLAND"+i+": "+ "towers: "+island.getTowerCount()+" " + island.getTowerColour().toString()
            + " -- students: " + island.getStudents());
            if(island.isHasMotherNature()){
                System.out.print(" -- MOTHER NATURE ON");
            }
            if(island.getNumNoEntryTile()>0)
                System.out.print(" -- "+ island.getNumNoEntryTile() + " no entry tile(s)");
            i++;
        }
        for(Cloud cloud: gameState.getClouds())
            System.out.println("cloud "+cloud.getCloudId()+ ": " + cloud.getStudents());
        if(gameState.isExpert()){
            int i=0;
            for(Character character: gameState.getCharacters())
            System.out.println("character card "+i+": cost: "+character.getActualCost());
            //System.out.println() ogg. sulla carta
        }
    }

}

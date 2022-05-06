package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.common.gamePojo.*;

import java.util.Scanner;

public class VIEWclientCLI {
    private static Scanner scanner;

    static{ VIEWclientCLI.scanner = new Scanner(System.in);}

    //ATTENZIONE: ANCORA DA AGGIUNGERE QUI TUTTI I SYSTEM.OUT IN GIRO NELLE ALTRE CLASSI

    /** read int from System.in, continues to ask as long as the user doesn't write a number */
    public static int readInt(){
        int val=0;
        boolean valid;
        do {
            valid=false;
            if(scanner.hasNextInt()) {
                val = scanner.nextInt();
                valid = true;
            }else{
                scanner.next();
                System.out.println("Input not valid");
            }
        }while(!valid);
        return val;
    }

    public static String readString(){
        return scanner.nextLine();
    }


    public static void show(GameStatePojo gameStatePojo){
        for(PlayerPojo playerPojo : gameStatePojo.getPlayers())
            System.out.println(playerPojo.getNickname() + " : turno: " + playerPojo.getPlayedAssistantCard().getTurnOrder() + " - madre natura: " + playerPojo.getPlayedAssistantCard().getMovementsMotherNature());
        for(PlayerPojo playerPojo : gameStatePojo.getPlayers()){
            System.out.println("\n\t\t\t\t" + playerPojo.getNickname() + " SCHOOLBOARD");
            System.out.println("ENTRANCE:" + playerPojo.getSchoolBoard().getEntrance());
            System.out.println("DINING ROOM:"+ playerPojo.getSchoolBoard().getDiningRoom());
            System.out.println("PROFESSORS:" + playerPojo.getSchoolBoard().getProfessors());
            System.out.println("NUM TOWERS:" + playerPojo.getSchoolBoard().getSpareTowers());
        }

        for(IslandPojo islandPojo : gameStatePojo.getIslands()){
            int i=0;
            System.out.println("ISLAND"+i+": "+ "towers: "+ islandPojo.getTowerCount()+" " + islandPojo.getTowerColour().toString()
                    + " -- students: " + islandPojo.getStudents());
            if(islandPojo.isHasMotherNature()){
                System.out.print(" -- MOTHER NATURE ON");
            }
            if(islandPojo.getNumNoEntryTile()>0)
                System.out.print(" -- "+ islandPojo.getNumNoEntryTile() + " no entry tile(s)");
            i++;
        }
        for(CloudPojo cloudPojo : gameStatePojo.getClouds())
            System.out.println("cloud "+ cloudPojo.getCloudId()+ ": " + cloudPojo.getStudents());
        if(gameStatePojo.isExpert()){
            int i=0;
            for(CharacterPojo characterPojo : gameStatePojo.getCharacters())
                System.out.println("character card "+i+": cost: "+ characterPojo.getActualCost());
            //System.out.println() ogg. sulla carta
        }
    }
}
package it.polimi.ingsw.server.controller.persistence;

import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.server.controller.network.ServerController;
import it.polimi.ingsw.server.model.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SavingsMenu {

    private HashMap<Integer, List<String>> gamesMap = new HashMap<>();

    public HashMap<Integer, List<String>> getGamesMap() {
        return gamesMap;
    }

    private Integer getGameId(List<String> playersNicknames){
        for(Integer k : gamesMap.keySet()){
            List<String> list = gamesMap.get(k);
            if (compereLists(playersNicknames, list)){
                return k;
            }
        }
        return null;
    }

    private boolean compereLists(List<String> list1, List<String> list2){
        boolean found = false;

        if (list1 != null){
            if (list2 != null){
                for (int i = 0; i < list1.size(); i++){
                    found = false;
                    for (int j = 0; j < list2.size() && found == false; j++){
                        if (list1.get(i).equals(list2.get(j))){
                            found = true;
                        }
                    }
                    if (found == false){
                        return false;
                    }
                }
                for (int i = 0; i < list2.size(); i++){
                    found = false;
                    for (int j = 0; j < list1.size() && found == false; j++){
                        if (list2.get(i).equals(list1.get(j))){
                            found = true;
                        }
                    }
                    if (found == false){
                        return false;
                    }
                }
            } else {
                return false;
            }
        }else{
            if (list2 != null){
                return false;
            }
        }

        return true;
    }

    public void addGame(Integer k, List<Player> players){
        if (this.gamesMap.get(k) == null){
            List<String> playersNicknames = new ArrayList<>();
            for (Player p : players){
                playersNicknames.add(p.getNickname());
            }
            this.gamesMap.put(k, playersNicknames);
        }

        try {
            String currentPath = new File(".").getCanonicalPath();
            String fileName = currentPath + "/src/main/Resources/savings/SavingsMenu.txt";
            File file = new File(fileName);
            file.createNewFile();
            String dataString = JsonConverter.fromSavingsMenuToJson(this);
            FileOutputStream outputStream = new FileOutputStream(file, false);
            byte[] strToBytes = dataString.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e ){
            e.printStackTrace();
        }
    }

    public Saving getSavingFromNicknames(List<String> players){
        Integer gameId;
        Saving saving;
        SavingsMenu savingsMenu;


        String data = new String();
        try {
            String currentPath = new File(".").getCanonicalPath();
            String fileName = currentPath + "/src/main/Resources/savings/SavingsMenu.txt";
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = data + myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        savingsMenu = JsonConverter.fromJsonToSavingsMenu(data);
        this.gamesMap = savingsMenu.gamesMap;
        if (this.getGameId(players) == null){
            return null;
        }
        gameId = this.getGameId(players);

        data = new String();
        try {
            String currentPath = new File(".").getCanonicalPath();
            String fileName = currentPath + "/src/main/Resources/savings/Game" + gameId + ".txt";
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = data + myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        saving = JsonConverter.fromJsonToSaving(data);

        return saving;
    }
}

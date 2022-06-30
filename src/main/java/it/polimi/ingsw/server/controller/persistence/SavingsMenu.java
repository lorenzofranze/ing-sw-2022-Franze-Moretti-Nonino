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

/**This class contians a map of all the savings and their players. Moreover contains the GameControllerID of the
 * GameController class  so that if the server goes down, this information can be reloaded*/
public class SavingsMenu {

    private int gameControllerID = 1;

    private HashMap<Integer, List<String>> gamesMap = new HashMap<>();

    public HashMap<Integer, List<String>> getGamesMap() {
        return gamesMap;
    }

    /**returns the GameId of the game whose players' nicknames are the one indicated in the list
     * @param playersNicknames
     * */
    private Integer getGameId(List<String> playersNicknames){
        for(Integer k : gamesMap.keySet()){
            List<String> list = gamesMap.get(k);
            if (compereLists(playersNicknames, list)){
                return k;
            }
        }
        return null;
    }

    /**given two lists of nickanmes returns true if they contain the same nicknames, false in the other case
     * @param list1
     * @param list2
     * */
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

    /**given a GameId and a list of players, it checks if the savingsMenu has already that game. If not, it inserts the game
     *  and its players to the savingsMenu and updates the SavingsMenu.txt file
     *  @param k
     *  @param players
     *  */
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
            String fileName = currentPath + "/savings/SavingsMenu.txt";
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

    /**given a GameId, it checks if the savingsMenu has already that game. If so, it removes the game
     *  and its players from the savingsMenu and updates the SavingsMenu.txt file
     *  @param k
     *  */
    public void removeGame(Integer k){
        if (this.gamesMap.get(k) != null){
            this.gamesMap.remove(k);
            try {
                String currentPath = new File(".").getCanonicalPath();
                String fileName = currentPath + "/savings/Game"+k+".txt";
                File file = new File(fileName);
                file.delete();
            } catch (IOException e ){
                e.printStackTrace();
            }
        }

        try {
            String currentPath = new File(".").getCanonicalPath();
            String fileName = currentPath + "/savings/SavingsMenu.txt";
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

    /**given a list of players, it checks if savingsMenu contains a game with those players. If it does, it returns the
     * saving of that game; if not returns null
     *  @param players
     *  */
    public Saving getSavingFromNicknames(List<String> players){
        Integer gameId;
        Saving saving;
        SavingsMenu savingsMenu;


        String data = new String();
        try {
            String currentPath = new File(".").getCanonicalPath();
            String fileName = currentPath + "/savings/SavingsMenu.txt";
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
            String fileName = currentPath + "/savings/Game" + gameId + ".txt";
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

    public int getGameControllerID() {
        return gameControllerID;
    }

    public void setGameControllerID(int gameControllerID) {
        this.gameControllerID = gameControllerID;
    }
}

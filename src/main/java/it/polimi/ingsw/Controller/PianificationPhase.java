package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.*;

import it.polimi.ingsw.Controller.GamePhase;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

public class PianificationPhase implements GamePhase {

    private GameController gameController;
    private Player firstPlayer;

    public PianificationPhase(GameController gc){
        this.gameController = gc;
    }

    @Override
    public void handle(){

        fillClouds();

        playAssistantCards();


    }

    private void fillClouds(){

        int numberOfPlayers = this.gameController.getGame().getPlayers().size();
        PawnsMap toAdd;

        for(Cloud c: gameController.getGame().getClouds()){
            if (numberOfPlayers == 2) {
                toAdd = this.gameController.getGame().getStudentsBag().removeRandomly(3);}
            else {
                toAdd = this.gameController.getGame().getStudentsBag().removeRandomly(4);}

            c.getStudents().add(toAdd);
        }
    }

    private void playAssistantCards(){
        //index of the first player
        int playerIndex = this.gameController.getGame().getPlayers().indexOf(firstPlayer);
        int numberOfPlayers = this.gameController.getGame().getPlayers().size();

        //turnOrder of the card played
        int played;

        MessageHandler messageHandler = this.gameController.getMessageHandler();
        Player currentPlayer;

        HashMap<Player, Integer> maximumMovements = new HashMap<Player, Integer>();
        List<Player> nextTurnOrder = new ArrayList<>();

        //initialization maximumMovements
        List<Player> list = this.gameController.getGame().getPlayers();
        for(Player p : list){
            maximumMovements.put(p, 0);
        }

        /*support hashmap and list. The list is used to keep track of the order in which players played: in case 2
        players have the same value played as nextTurn, in the actionPhase the first player will be the one who has
        played before*/
        HashMap<Player, Integer> playerTurn = new HashMap<Player, Integer>();
        list.clear();

        for(int i = 0; i < numberOfPlayers; i++){
            currentPlayer = this.gameController.getGame().getPlayers().get((playerIndex + i) % numberOfPlayers);
            played = messageHandler.getValue(currentPlayer);
            for(AssistantCard c :currentPlayer.getDeck())
            {
                if (c.getTurnOrder() == played) {
                    maximumMovements.put(currentPlayer, c.getMovementsMotherNature());
                }
            }
            playerTurn.put(currentPlayer, played);
            list.add(currentPlayer);
            currentPlayer.playAssistantCard(played);
        }


        for(int i = 1; i < 11; i++){
            for(Player p : list){
                if (i == (int) playerTurn.get(p)) {
                    nextTurnOrder.add(p);
                }
            }
        }


        /*at this point playerTurn is an ordered list of the players for the actionphase and maximumMovements is a map
        that associates players to their maximum movements.*/

        ActionPhase a = (ActionPhase) this.gameController.getActionPhase();
        a.setMaximumMovements(maximumMovements);
        a.setTurnOrder(nextTurnOrder);

        this.gameController.setGamePhase(gameController.getActionPhase());
    }

    public void setFirstPlayer(Player player){
        this.firstPlayer = player;
    }

}

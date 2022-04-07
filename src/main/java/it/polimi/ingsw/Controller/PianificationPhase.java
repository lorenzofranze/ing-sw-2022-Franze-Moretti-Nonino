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

        int playerIndex = this.gameController.getGame().getPlayers().indexOf(firstPlayer);
        int numberOfPlayers = this.gameController.getGame().getPlayers().size();

        fillClouds();

        /*support hashmap and list. The list is used to keep track of the order in which players played: in case 2
        players have the same value played as nextTurn, in the actionPhase the first player will be the one who has
        played before*/
        List<Player> playedOrder = new ArrayList<>();
        HashMap<Player, Integer> turnOrder = new HashMap<Player, Integer>();
        HashMap<Player, Integer> maximumMovements = new HashMap<Player, Integer>();

        List<Player> nextTurnOrder = new ArrayList<>();
        Player currentPlayer;

        //initialize
        for(int i = 0; i < numberOfPlayers; i++){
            currentPlayer = this.gameController.getGame().getPlayers().get(i);
            currentPlayer.resetAssistantCard();
        }

        for(int i = 0; i < numberOfPlayers; i++){
            currentPlayer = this.gameController.getGame().getPlayers().get((playerIndex + i) % numberOfPlayers);
            this.gameController.setCurrentPlayer(currentPlayer);
            playAssistantCard(currentPlayer, turnOrder, maximumMovements);
            playedOrder.add(currentPlayer);
        }

        for(int i = 1; i < 11; i++){
            for(Player p : playedOrder){
                if (i == (int) turnOrder.get(p)) {
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

    private void playAssistantCard(Player currentPlayer, HashMap<Player, Integer> turnOrder, HashMap<Player,
            Integer> maximumMovements){

        AssistantCard cardPlayed = null;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        boolean mustChange = false;
        boolean valid = false;

        do {
            mustChange = false;
            valid = false;

            int played = messageHandler.getValue(currentPlayer);

            for (AssistantCard c : currentPlayer.getDeck()) {
                if (c.getTurnOrder() == played) {
                    cardPlayed = c;
                    valid = true;
                }
            }

            for (int i = 0; i < this.gameController.getGame().getPlayers().size(); i++) {
                Player p = this.gameController.getGame().getPlayers().get(i);
                if (p.getPlayedAssistantCard().equals(cardPlayed)) {
                    mustChange = checkPermit(currentPlayer, cardPlayed);
                }
            }

            if (mustChange == false) {
                turnOrder.put(currentPlayer, played);
                maximumMovements.put(currentPlayer, cardPlayed.getMovementsMotherNature());
                currentPlayer.playAssistantCard(played);
            }
        }
        while(valid == false || mustChange == true);
            /*if valid == false, the player doensn't have that card in his deck / the card doesn't exist.
            * if mustChange == true, the player played a card that has already been played by other players.*/
    }

    public void setFirstPlayer(Player player){
        this.firstPlayer = player;
    }


    /**if a player plays a card already played, checkPemit checks if he/she can play another card instead.
     * Returns true if she/he cannot. Otherwise false.*/
    public boolean checkPermit(Player player, AssistantCard card){
        boolean temp = false;

        for(AssistantCard c : player.getDeck()) {
            temp = false;
            for(Player p : this.gameController.getGame().getPlayers()){
                if(p.getPlayedAssistantCard().equals(c)){
                    temp = true;
                }
            }
            if (temp = false) {return false;}
        }
        return true;
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

}

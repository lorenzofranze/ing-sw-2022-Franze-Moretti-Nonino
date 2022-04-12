package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.*;

import java.util.HashMap;
import java.util.List;
import java.util.*;

public class PianificationPhase extends GamePhase {

    private GameController gameController;
    private boolean isLastRoundFinishedAssistantCards;
    private boolean isLastRoundFinishedStudentsBag;


    public PianificationPhase(GameController gameController){
        this.gameController = gameController;
    }

    public PianificationResult handle(Player firstPlayer){

        //index of the first player in the list of players contained in game
        int playerIndex = this.gameController.getGame().getPlayers().indexOf(firstPlayer);
        int numberOfPlayers = this.gameController.getGame().getPlayers().size();


        /*support hashmap and list. The list is used to keep track of the order in which players played: in case 2
        players have the same value played as nextTurn, in the actionPhase the first player will be the one who has
        played before*/
        List<Player> playedOrder = new ArrayList<>();
        HashMap<Player, Integer> turnOrderMap = new HashMap<Player, Integer>();
        HashMap<Player, Integer> maximumMovements = new HashMap<Player, Integer>();

        List<Player> turnOrder = new ArrayList<>();
        Player currentPlayer;

        //initialize
        for(int i = 0; i < numberOfPlayers; i++){
            currentPlayer = this.gameController.getGame().getPlayers().get(i);
            currentPlayer.resetAssistantCard();
        }

        for(int i = 0; i < numberOfPlayers; i++){
            currentPlayer = this.gameController.getGame().getPlayers().get((playerIndex + i) % numberOfPlayers);
            this.gameController.setCurrentPlayer(currentPlayer);
            playAssistantCard(currentPlayer, turnOrderMap, maximumMovements);
            playedOrder.add(currentPlayer);
        }


        /*turnOrder sarà la lista dei giocatori ordinata come giocheranno la ActionPhase. Se due giocatori hanno
        lo stesso turnOrder, allora il giocatore che ha gocato dopo la carta verrà posto dopo il giocatore che
        l'ha giocata prima. */
        for(int i = 1; i < 11; i++){
            for(Player p : playedOrder){
                if (i == (int) turnOrderMap.get(p)) {
                    turnOrder.add(p);
                }
            }
        }

        /*at this point turnOrder is an ordered list of the players for the actionphase and maximumMovements is a map
        that associates players to their maximum movements.*/

        fillClouds();



        PianificationResult pianificationResult = new PianificationResult();
        pianificationResult.setMaximumMovements(maximumMovements);
        pianificationResult.setTurnOrder(turnOrder);

        return pianificationResult;

    }

    private void playAssistantCard(Player currentPlayer, HashMap<Player, Integer> turnOrderMap, HashMap<Player,
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
                turnOrderMap.put(currentPlayer, played);
                maximumMovements.put(currentPlayer, cardPlayed.getMovementsMotherNature());
                currentPlayer.playAssistantCard(played);
            }
        }
        while(valid == false || mustChange == true);
            /*if valid == false, the player doensn't have that card in his deck / the card doesn't exist.
            * if mustChange == true, the player played a card that has already been played by other players.*/
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
        String result;

        for(Cloud c: gameController.getGame().getClouds()){
            if (numberOfPlayers == 2) {
                if(gameController.getGame().getStudentsBag().pawnsNumber()<3) {
                    return;
                }
                toAdd = this.gameController.getGame().getStudentsBag().removeRandomly(3);}
            else {
                if(gameController.getGame().getStudentsBag().pawnsNumber()<4) {
                    return;
                }
                toAdd = this.gameController.getGame().getStudentsBag().removeRandomly(4);}

            c.getStudents().add(toAdd);
        }

        return;
    }

}

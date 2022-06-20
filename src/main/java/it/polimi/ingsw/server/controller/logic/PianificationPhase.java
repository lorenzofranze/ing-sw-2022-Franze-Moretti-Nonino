package it.polimi.ingsw.server.controller.logic;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.PawnsMap;
import it.polimi.ingsw.server.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.*;

public class PianificationPhase extends GamePhase {

    private GameController gameController;
    private boolean finishedAssistantCard;
    private boolean finishedStudentBag;


    public PianificationPhase(GameController gameController){
        this.gameController = gameController;
    }

    /**
     * Fills the clouds, asks the player to choose an assistant card,
     * creates a list of the players ordered for the action turns and
     * checks for finishedAssistantCard and for finishedStudentBag.
     * - turnOrderMap is the map which keeps track of the order of the players in the actionPhase.
     * - maximumMovements is a map that associates players to their maximum movements
     *
     * @param firstPlayer
     * @return
     */
    public PianificationResult handle(Player firstPlayer){

        //index of the first player in the list of players contained in game
        int playerIndex = this.gameController.getGame().getPlayers().indexOf(firstPlayer);
        int numberOfPlayers = this.gameController.getGame().getPlayers().size();


        /**
         * playedOrder supports turnOrderMap and turnOrder list. The playedOrder is used to keep track of the order
         * in which players has played the pianification phase:
         * in case 2 players have the same value played as nextTurn, in the actionPhase the first
         * player will be the one who has played before
         */
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

        this.fillClouds();

        for(int i = 0; i < numberOfPlayers; i++){
            currentPlayer = this.gameController.getGame().getPlayers().get((playerIndex + i) % numberOfPlayers);
            this.gameController.setCurrentPlayer(currentPlayer);
            gameController.update();
            playAssistantCard(currentPlayer, turnOrderMap, maximumMovements);
            playedOrder.add(currentPlayer);
        }

        /*turnOrder sarà la lista dei giocatori ordinata come giocheranno la ActionPhase. Se due giocatori hanno
        lo stesso turnOrder, allora il giocatore che ha gocato dopo la carta verrà posto dopo il giocatore che
        l'ha giocata prima. */
        for(int i = 1; i < 11; i++){
            for(Player p : playedOrder){
                if (i == (int) turnOrderMap.get(p)) {turnOrder.add(p);}
            }
        }



        PianificationResult pianificationResult = new PianificationResult();
        pianificationResult.setMaximumMovements(maximumMovements);
        pianificationResult.setTurnOrder(turnOrder);
        pianificationResult.setFinishedAssistantCard(finishedAssistantCard);
        pianificationResult.setFinishedStudentBag(finishedStudentBag);

        return pianificationResult;
    }

    /**
     * Manages the choice of the assistant card:
     * first reads the message received from the player, then
     * if an other player has alrealy played this card in this round, sends an AlreadyPlayed message,
     * if the player doensn't have that card in his deck because he has already played this card, sends an InvalidChoice
     * message,
     * if the choice is valid, the card is played, it is sent an ack message and it is memorized the maximum movements
     * the player can move mother nature according to the card chosen
     * @param currentPlayer
     * @param turnOrderMap
     * @param maximumMovements
     */
    public void playAssistantCard(Player currentPlayer, HashMap<Player, Integer> turnOrderMap, HashMap<Player,
            Integer> maximumMovements){

        AssistantCard cardPlayed = null;
        GameMessage gameMessage;
        Message fromClient;
        ErrorMessage gameErrorMessage;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        boolean mustChange = false;
        boolean isInDeck = false;
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);

        do {
            mustChange = false;
            isInDeck = false;

            Message message = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.AssistantCard);
            Integer played = null;

            if (message != null){
                gameMessage = (GameMessage) message;
                played= gameMessage.getValue();
            }else{
                return;
            }

            for (AssistantCard c : currentPlayer.getDeck()) {
                if (c.getTurnOrder() == played) {
                    cardPlayed = c;
                    isInDeck = true;
                }
            }

            if (isInDeck){
                for(Player p: this.gameController.getGame().getPlayers()){
                    if (!p.equals(currentPlayer) && p.getPlayedAssistantCard() != null &&
                            p.getPlayedAssistantCard().equals(cardPlayed)){
                        mustChange = !checkPermit(currentPlayer, cardPlayed);
                    }
                }

                if (!mustChange) {
                    AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
                    playerManager.sendMessage(ackMessage);
                    turnOrderMap.put(currentPlayer, played);
                    maximumMovements.put(currentPlayer, cardPlayed.getMovementsMotherNature());
                    currentPlayer.playAssistantCard(played);
                }else{
                    gameErrorMessage = new ErrorMessage(TypeOfError.AlreadyPlayed); // an other player has alrealy played this card in this round
                    playerManager.sendMessage(gameErrorMessage);
                }
            }
            else{
                gameErrorMessage = new ErrorMessage(TypeOfError.InvalidChoice); // You have already played this card
                playerManager.sendMessage(gameErrorMessage);
            }
        }
        while(isInDeck == false || mustChange == true);
        /*if valid == false, the player doensn't have that card in his deck / the card doesn't exist.
         * if mustChange == true, the player played a card that has already been played by other players.*/

        if (currentPlayer.getDeck().size() == 0){finishedAssistantCard = true;}
    }


    /**
     * If a player plays a card already played, checkPemit checks if he/she can play another card instead.
     * Returns true if she/he cannot. Otherwise false.*/
    public boolean checkPermit(Player player, AssistantCard card){
        boolean temp = true;
        boolean isPresent = false;

        for(AssistantCard c : player.getDeck()) {
            if (temp == false){break;}
            isPresent = false;
            for(Player p : this.gameController.getGame().getPlayers()){
                if(p.getPlayedAssistantCard()!= null && p.getPlayedAssistantCard().equals(c) && isPresent==false){
                    isPresent = true;
                }
            }
            if (isPresent == false){
                temp = false;
            }
        }

        return temp;
    }

    /**
     * Fills the clouds with students:
     * if the numberOfPlayers is 2, 3 students are put on each clous,
     * otherwise, if the numberOfPlayers is 3, 4 students are put on each clous.
     */
    public void fillClouds(){

        int numberOfPlayers = this.gameController.getGame().getPlayers().size();
        PawnsMap toAdd;
        String result;

        for(Cloud c: gameController.getGame().getClouds()){
            if (numberOfPlayers == 2) {
                if(gameController.getGame().getStudentsBag().pawnsNumber()<3) {
                    finishedStudentBag=true;
                    return;
                }
                toAdd = this.gameController.getGame().getStudentsBag().removeRandomly(3);}
            else {
                if(gameController.getGame().getStudentsBag().pawnsNumber()<4) {
                    finishedStudentBag=true;
                    return;
                }
                toAdd = this.gameController.getGame().getStudentsBag().removeRandomly(4);}

            c.getStudents().add(toAdd);
        }


        return;
    }

    @Override
    public String toString(){
        return "PianificationPhase";
    }

    public boolean isFinishedStudentBag() {
        return finishedStudentBag;
    }
}

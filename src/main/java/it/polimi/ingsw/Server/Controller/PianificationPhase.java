package it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.IntMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Model.AssistantCard;
import it.polimi.ingsw.Server.Model.Cloud;
import it.polimi.ingsw.Server.Model.PawnsMap;
import it.polimi.ingsw.Server.Model.Player;

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
        pianificationResult.setFinishedAssistantCard(finishedAssistantCard);
        pianificationResult.setFinishedStudentBag(finishedStudentBag);

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
            gameController.update();

            ServerMessage messageToSend= new ServerMessage(currentPlayer.getNickname(), TypeOfMessage.AssistantCard);
            IntMessage receivedMessage = (IntMessage) messageHandler.communicationWithClient(gameController, messageToSend);
            int played=receivedMessage.getValue();


            for (AssistantCard c : currentPlayer.getDeck()) {
                if (c.getTurnOrder() == played) {
                    cardPlayed = c;
                    valid = true;
                }
            }

            if (valid){

                for(Player p: this.gameController.getGame().getPlayers()){
                    if (!p.equals(currentPlayer) && p.getPlayedAssistantCard() != null &&
                            p.getPlayedAssistantCard().equals(cardPlayed)){
                        mustChange = !checkPermit(currentPlayer, cardPlayed);
                    }
                }

                if (mustChange == false) {
                    turnOrderMap.put(currentPlayer, played);
                    maximumMovements.put(currentPlayer, cardPlayed.getMovementsMotherNature());
                    currentPlayer.playAssistantCard(played);
                }else{
                    messageHandler.stringMessageToClient(gameController,"an other player has alrealy played this card in this round, rechoose.",currentPlayer.getNickname());
                    //System.out.println("an other player has alrealy played this card in this round, rechoose.");
                }

            }
            else{
                messageHandler.stringMessageToClient(gameController,"\"You have already played this card, rechoose.\"",currentPlayer.getNickname());
                //System.out.println("You have already played this card, rechoose.");
            }
        }
        while(valid == false || mustChange == true);
            /*if valid == false, the player doensn't have that card in his deck / the card doesn't exist.
            * if mustChange == true, the player played a card that has already been played by other players.*/

        gameController.update();

        if (currentPlayer.getDeck().size() == 0){
            finishedAssistantCard = true;
        }

    }


    /**if a player plays a card already played, checkPemit checks if he/she can play another card instead.
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

    private void fillClouds(){

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

}

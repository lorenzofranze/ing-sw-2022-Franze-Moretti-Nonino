package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Controller.Characters.CharacterEffect;
import it.polimi.ingsw.Server.Controller.Network.Lobby;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.GameStateMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.Message;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Character;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.utils.Observable;

import java.util.*;

public class GameController implements Runnable  {
    private static int gameID = 1;

    private SetUpPhase setUpPhase;
    private PianificationPhase pianificationPhase;
    private ActionPhase actionPhase;

    private GamePhase currentPhase;

    private boolean gameOver=false;

    private boolean isLastRoundFinishedAssistantCards = false;
    private boolean isLastRoundFinishedStudentsBag = false;
    private boolean isFinishedTowers = false;
    private boolean isThreeOrLessIslands = false;

    private Game game;
    private boolean expert;
    private MessageHandler messageHandler;
    private Player currentPlayer;
    private Player winner;

    private PianificationResult pianificationResult;
    private ActionResult actionResult = null;


    private Map<Character, CharacterEffect> characterEffects; // per gli effetti

    public GameController(Lobby lobby, boolean expert){
        this.game=new Game(lobby.getUsersNicknames(), this.gameID);
        this.expert=expert;
        this.gameID ++;
        this.characterEffects = new HashMap<>();

        this.messageHandler= new MessageHandler(lobby);
    }

    public void run(){

        for(Player p: game.getPlayers()){
            messageHandler.stringMessageToClient(this, "THE GAME IS STARTING", p.getNickname());
        }

        this.setUpPhase=new SetUpPhase(this);
        this.pianificationPhase=new PianificationPhase(this);
        this.actionPhase=new ActionPhase(this);

        currentPhase = setUpPhase;
        SetUpResult setUpResult = setUpPhase.handle();


        currentPlayer = setUpResult.getFirstRandomPianificationPlayer();

        do{
            currentPhase = pianificationPhase;
            if (actionResult!=null) {
                currentPlayer = actionResult.getFirstPianificationPlayer();
            }

            for(Player p: game.getPlayers()){
                messageHandler.stringMessageToClient(this, "PIANIFICATION PHASE", p.getNickname());
            }
            //System.out.println("\n--------------------------------------PIANIFICATION PHASE----------------------------------------\n");

            pianificationResult = this.pianificationPhase.handle(currentPlayer);

            isLastRoundFinishedAssistantCards = pianificationResult.isFinishedAssistantCard();
            isLastRoundFinishedStudentsBag = pianificationResult.isFinishedStudentBag();

            currentPhase = actionPhase;
            HashMap<Player, Integer> maximumMovements = pianificationResult.getMaximumMovements();
            List<Player> turnOrder = pianificationResult.getTurnOrder();

            for(Player p: game.getPlayers()){
                messageHandler.stringMessageToClient(this, "ACTION PHASE", p.getNickname());
            }
            //System.out.println("\n--------------------------------------ACTION PHASE----------------------------------------\n");

            actionResult = this.actionPhase.handle(turnOrder, maximumMovements, isLastRoundFinishedStudentsBag);
            isFinishedTowers = actionResult.isFinishedTowers();
            isThreeOrLessIslands = actionResult.isThreeOrLessIslands();

        }
        while(!(isFinishedTowers || isThreeOrLessIslands || isLastRoundFinishedStudentsBag || isLastRoundFinishedAssistantCards));

        for(Player p: game.getPlayers()){
            messageHandler.stringMessageToClient(this, "GAME ENDED", p.getNickname());
        }
        //System.out.println("\n--------------------------------------GAME ENDED----------------------------------------\n");

        calculateWinner(); //se winner è null, allora la partita è finita in pareggio

        if (winner != null){
            for(Player p: game.getPlayers()){
                messageHandler.stringMessageToClient(this, "The winner is " + this.winner.toString(), p.getNickname());
            }
            //System.out.println("The winner is " + this.winner.toString());
        }
        else{
            for(Player p: game.getPlayers()){
                messageHandler.stringMessageToClient(this, "There is no winner.", p.getNickname());
            }
            //System.out.println("There is no winner.");
        }



        for(Player p: game.getPlayers()) {
            messageHandler.stringMessageToClient(this, "Students left in Studentbag:" + this.game.getStudentsBag().pawnsNumber(), p.getNickname());
            for (Player player : this.game.getPlayers()){
                messageHandler.stringMessageToClient(this,player.getNickname()+": " + player.getSchoolBoard().getSpareTowers() + " towers left on schoolboard", p.getNickname());
            }
        }
        //System.out.println("Students left in Studentbag:" + this.game.getStudentsBag().pawnsNumber());
        //for(Player p : this.game.getPlayers())
        //    System.out.println(p.getNickname()+": " + p.getSchoolBoard().getSpareTowers() + " towers left on schoolboard");

    }



    public void update(){

        Map<Player, Boolean> updatedPlayers = new HashMap<Player, Boolean>();
        for(Player p : game.getPlayers()){
            updatedPlayers.put(p, false);
        }

        for(Player p: this.game.getPlayers()){
            GameStateMessage gameStateMessage= new GameStateMessage(p.getNickname(), TypeOfMessage.GameState, this);
            messageHandler.sendUpdate(this, gameStateMessage);
            //??
            if (ack.getMessageType() == TypeOfMessage.GameStateACK){
                updatedPlayers.put(p, true);
            }else{
                System.out.println("Messaggio ricevuto non valido");
            }
        }

        boolean allPlayersUpdated = true;
        for(Boolean b : updatedPlayers.values()){
            if (b == false){
                allPlayersUpdated = false;
            }
        }

        if(allPlayersUpdated) {
            for (Player p : this.game.getPlayers()) {
                messageHandler.stringMessageToClient(this, "EVERYONE UPDATED", p.getNickname());
            }
        }

        return;
    }



    /**sets the GameController.winner
     * if it remains null, it means ther is no winner*/
    public void calculateWinner(){

       /*devo controllare nello stesso ordine in cui si arresta il gioco:
       1. un giocatore finisce le torri
       2. quante torri sono state piazzate per ciascun giocatore */

        //1. controllo se un giocatore ha piazzato tutte le torri

        for(Player p : getGame().getPlayers()){
            if (p.getSchoolBoard().getSpareTowers() <= 0){
                winner = p;
                return;
            }
        }

        //calcolo il vincitore contando le torri piazzate

        //map of the players and the number of tower that they have placed
        Map<Player, Integer> towerPlaced = new HashMap<Player, Integer>();
        for(Player p : getGame().getPlayers()){
            towerPlaced.put(p, 0);
        }

        for(Island i : getGame().getIslands()){
            if(i.getOwner(getGame()) != null)
                towerPlaced.put(i.getOwner(getGame()), towerPlaced.get(i.getOwner(getGame())) + i.getTowerCount());
        }

        int maxPlaced = 0;
        Player tempWinner = null;
        for(Player p : getGame().getPlayers()){
            if (towerPlaced.get(p) > maxPlaced){
                maxPlaced = towerPlaced.get(p);
                tempWinner = p;
            }
        }

        //verifico che non ci siano altri giocatori con lo stesso numero di torri piazzate
        boolean isOnly = true;
        for(Player p : getGame().getPlayers()){
            if (towerPlaced.get(p) == maxPlaced && !p.equals(tempWinner)){
                isOnly = false;
            }
        }
        if (isOnly == true){
            winner = tempWinner;
            return;
        }

        //nel caso ci sia parità di torri piazzate conto il numero dei professori

        int maxProf = 0;
        tempWinner = null;
        for(Player p : getGame().getPlayers()){
            if (p.getSchoolBoard().getProfessors().pawnsNumber() > maxProf){
                maxProf = p.getSchoolBoard().getProfessors().pawnsNumber();
                tempWinner = p;
            }
        }

        //verifico che non ci siano altri giocatori con lo stesso numero di professori
        isOnly = true;
        for(Player p : getGame().getPlayers()){
            if (p.getSchoolBoard().getProfessors().pawnsNumber() == maxProf && !p.equals(tempWinner)){
                isOnly = false;
            }
        }
        if (isOnly == true){
            winner = tempWinner;
            return;
        }

    }

    public Game getGame() {
        return game;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Integer getGameID() {
        return gameID;
    }

    public Map<Character, CharacterEffect> getCharacterEffects() {
        return characterEffects;
    }

    /** used by Char4 doEffect()
     * it adds to the maximum movements of mother nature permitted by the assistant card chosen by the player
     * two additional steps
     * @param player
     */
    public void addTwoMovements(Player player){
        Integer numMax= pianificationResult.getMaximumMovements().get(player)+2;
        HashMap<Player,Integer> newMaxMovements= pianificationResult.getMaximumMovements();
        newMaxMovements.put(player,numMax);
        pianificationResult.setMaximumMovements(newMaxMovements);
    }

    public ActionPhase getActionPhase() {
        return actionPhase;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }
}

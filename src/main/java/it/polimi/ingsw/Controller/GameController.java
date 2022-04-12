package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameController {

    private static Map<Integer, GameController> instances = null; //Integer is the gameId of that GameController

    private Random rand = new Random(); //utilizzato per creare randomicamente gli id ai nuovi game
    private Integer gameID;

    private SetUpPhase setUpPhase;
    private PianificationPhase pianificationPhase;
    private ActionPhase actionPhase;

    private GamePhase currentPhase;

    private boolean gameOver=false;

    private boolean isLastRoundFinishedAssistantCards;
    private boolean isLastRoundFinishedStudentsBag;
    private boolean isFinishedTowers;
    private boolean isThreeOrLessIslands;


    private Game game;
    private boolean expert;
    private MessageHandler messageHandler;
    private Player currentPlayer;
    private Player winner;

    public void run(){

        currentPhase = setUpPhase;
        SetUpResult setUpResult = setUpPhase.handle();

        PianificationResult pianificationResult;
        ActionResult actionResult = null;
        Player firstPlayer = setUpResult.getFirstRandomPianificationPlayer();

        do{
            currentPhase = pianificationPhase;
            if (! actionResult.equals(null)) {
                firstPlayer = actionResult.getFirstPianificationPlayer();
            }

            pianificationResult = this.pianificationPhase.handle(firstPlayer);

            checkLastRound();

            currentPhase = actionPhase;
            HashMap<Player, Integer> maximumMovements = pianificationResult.getMaximumMovements();
            List<Player> turnOrder = pianificationResult.getTurnOrder();

            actionResult = this.actionPhase.handle(turnOrder, maximumMovements, isLastRoundFinishedStudentsBag);
            this.isFinishedTowers = actionResult.isFinishedTowers();
            this.isThreeOrLessIslands = actionResult.isThreeOrLessIslands();

        }
        while(!gameEnded());

        calculateWinner();
        //se winner è null, allora la partita è finita in pareggio
    }

    private boolean gameEnded(){
        boolean endgame = this.isFinishedTowers || this.isThreeOrLessIslands ||
                isLastRoundFinishedStudentsBag || isLastRoundFinishedAssistantCards;
        return endgame;
    }

    private void checkLastRound(){

        boolean atLeastOnePlayerWithNoCards = false;
        for(Player p: getGame().getPlayers()){
            if (p.getDeck().size() == 0){
                atLeastOnePlayerWithNoCards = true;
            }
        }

        boolean atLeastOneCloudNotFull = false;
        int numberOfPlayers = this.getGame().getPlayers().size();
        if (numberOfPlayers == 2){
            for(Cloud c: getGame().getClouds()){
                if (c.getStudents().pawnsNumber() != 3){
                    atLeastOneCloudNotFull = true;
                }
            }
        }
        if (numberOfPlayers == 3){
            for(Cloud c: getGame().getClouds()){
                if (c.getStudents().pawnsNumber() != 4){
                    atLeastOneCloudNotFull = true;
                }
            }
        }

        this.isLastRoundFinishedAssistantCards = atLeastOnePlayerWithNoCards;
        this.isLastRoundFinishedStudentsBag = atLeastOneCloudNotFull;

    }

    /*per ottenere il GameController con gameId = gameid devo fare GameController.getInstace(gameId)*/
    public static GameController getInstance(int gameID){
        return instances.get(gameID);
    }

    /*per creare un game controller data una lobby ed una modalità(esperto o semplice), devo fare
    /GameController.getInstance(lobby, expert). Il gameId viene generato automaticamente, se voglio lo posso
     chiedere con GameController.getGameID()*/
    public static GameController getInstance(Lobby lobby, boolean expert){
        if (instances == null){
            instances = new HashMap<Integer, GameController>();
        }

        GameController newGameController = new GameController(lobby, expert);
        Integer gameID = newGameController.getGameID();
        instances.put(gameID, newGameController);

        return newGameController;

    }

    private GameController(Lobby lobby, boolean expert){
        this.gameID = rand.nextInt();
        ///?????????????
        this.game=new Game(lobby.getUsersReadyToPlay().values(), this.gameID);
        this.expert=expert;

        this.setUpPhase=new SetUpPhase(this.gameID);
        this.pianificationPhase=new PianificationPhase(this.gameID);
        this.actionPhase=new ActionPhase(this.gameID);

        this.messageHandler= new MessageHandler();
    }



    public Game getGame() {
        return game;
    }

    public static Map<Integer, GameController> getInstances() {
        return instances;
    }

    /**sets the GameController.winner
     * if it remains null, it means ther is no winner*/
    public void calculateWinner(){

       /*devo controllare nello stesso ordine in cui si arresta il gioco:
       1. un giocatore finisce le torri
       2. quante torri sono state piazzate per ciascun giocatore
        */



        //controllo se un giocatore ha piazzato tutte le torri

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
}

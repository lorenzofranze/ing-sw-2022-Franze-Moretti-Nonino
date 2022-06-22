package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.AckMessage;
import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.TypeOfAck;
import it.polimi.ingsw.server.controller.characters.CharacterEffect;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.common.messages.UpdateMessage;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.controller.network.ServerController;
import it.polimi.ingsw.server.controller.persistence.Saving;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.Cloud;

import java.io.*;
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
    private Player winner=null;
    private Lobby lobby;
    private boolean forceStop;

    private PianificationResult pianificationResult;
    private ActionResult actionResult = null;

    private List<CharacterEffect> characterEffects = null; // per gli effetti

    /**
     * Each game has its own gameId (incremented progressively).
     * @param lobby
     * @param expert
     */
    public GameController(Lobby lobby, boolean expert){
        this.game=new Game(lobby.getUsersNicknames(), this.gameID);
        this.expert=expert;
        this.gameID ++;
        this.characterEffects = new ArrayList<>();
        this.lobby=lobby;
        this.messageHandler= new MessageHandler(lobby);
    }

    /**
     * Handles the setup phase just once and from the setUpResult gets the first random player for the pianification
     * phase. Handles the pianification phase and the action phase repetitevly.
     * From the pianificationResult understands the turn order for the action phase, the maximum movements for mother
     * nature for each player for the action phase and if it is the last round (for finished assistant cards or finished
     * students in bag).
     * From the actionResult understands if it is the last round due to three-or-less islands or for finished-towers for
     * one player.
     * Finally calculates the winner and stops the game.
     */
    public void run(){

        this.setUpPhase=new SetUpPhase(this);
        this.pianificationPhase=new PianificationPhase(this);
        this.actionPhase=new ActionPhase(this);

        currentPhase = setUpPhase;
        SetUpResult setUpResult = setUpPhase.handle();
        currentPlayer = setUpResult.getFirstRandomPianificationPlayer();
        AckMessage message = new AckMessage(TypeOfAck.CompleteLobby);
        messageHandler.sendBroadcast(message);

        update(); // update game start
        //lobby completa: inizio partita

        do{
            currentPhase = pianificationPhase;
            if (actionResult!=null) {
                currentPlayer = actionResult.getFirstPianificationPlayer();
            }

            //System.out.println("\n--------------------------------------PIANIFICATION PHASE----------------------------------------\n");

            pianificationResult = this.pianificationPhase.handle(currentPlayer);

            isLastRoundFinishedAssistantCards = pianificationResult.isFinishedAssistantCard();
            isLastRoundFinishedStudentsBag = pianificationResult.isFinishedStudentBag();

            currentPhase = actionPhase;
            HashMap<Player, Integer> maximumMovements = pianificationResult.getMaximumMovements();
            List<Player> turnOrder = pianificationResult.getTurnOrder();

            //System.out.println("\n--------------------------------------ACTION PHASE----------------------------------------\n");

            actionResult = this.actionPhase.handle(turnOrder, maximumMovements, isLastRoundFinishedStudentsBag);
            isFinishedTowers = actionResult.isFinishedTowers();
            isThreeOrLessIslands = actionResult.isThreeOrLessIslands();

        }
        while(!(isFinishedTowers || isThreeOrLessIslands || isLastRoundFinishedStudentsBag || isLastRoundFinishedAssistantCards || gameOver));

        //System.out.println("\n--------------------------------------GAME ENDED----------------------------------------\n");
        if(forceStop){
            return;
        }

        calculateWinner(); //se winner è "?", allora la partita è finita in pareggio

        update(); // last update: game ended and winner setted

        ServerController.getInstance().setToStop(this.getGameID());
    }


    /**
     * Sends update message for all the players.
     */
    public synchronized void update(){

        UpdateMessage updateMessage = new UpdateMessage(this.getGameState());
        for(PlayerManager playerManager : messageHandler.getPlayerManagerMap().values()){
            playerManager.sendMessage(updateMessage);
        }

        return;
    }

    /**
     * Sends update message for a specific player.
     * @param nickname
     */
    public synchronized void updateSinglePlayer(String nickname){
        PlayerManager playerManager= messageHandler.getPlayerManager(nickname);
        UpdateMessage updateMessage= new UpdateMessage(this.getGameState());
        playerManager.sendMessage(updateMessage);
        return;
    }



    /**
     * Sets the GameController.winner
     * if winner.nikname == "?", it means ther is no winner.
     * --> the winner is the player who has finished his towers first
     * --> the winner is the player who has the highest number of towers on the islands
     * --> if there is draw of number of towers on the islands, the winner is the player who has the highest number of
     * professors
     * */
    public void calculateWinner(){

       /*devo controllare nello stesso ordine in cui si arresta il gioco:
       1. un giocatore finisce le torri
       2. quante torri sono state piazzate per ciascun giocatore */

        //1. controllo se un giocatore ha piazzato tutte le torri

        for(Player p : getGame().getPlayers()){
            if (p.getSchoolBoard().getSpareTowers() <= 0){
                winner = p;
                gameOver=true;
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
            gameOver=true;
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
            gameOver=true;
            return;
        }

        gameOver = true;
        winner = new Player("?", null, null);


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

    /**
     * Sets the turn "true" only for the currentPlayer using the playerManagerMap in the MessageHandler()-class
     * @param currentPlayer
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        //it sets myTurn of all players in PlayerManager
        this.getMessageHandler().setTurn(currentPlayer.getNickname());
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Integer getGameID() {
        return gameID;
    }

    public List<CharacterEffect> getCharacterEffects() {
        return characterEffects;
    }

    public CharacterEffect getCharacterByID(int id){
        for(CharacterEffect characterEffect : characterEffects)
            if(characterEffect.getID() == id)
                return characterEffect;
        return null;
    }
    /**
     * Used by Char4 doEffect()
     * it adds to the maximum movements of mother nature permitted by the assistant card chosen by the player
     * two additional steps
     * @param player
     */
    public void addTwoMovements(Player player){
        Integer numMax = actionPhase.getMaximumMovements().get(player)+2;
        HashMap<Player,Integer> newMaxMovements= actionPhase.getMaximumMovements();
        newMaxMovements.put(player,numMax);
        actionPhase.setMaximumMovements(newMaxMovements);
    }

    public ActionPhase getActionPhase() {
        return actionPhase;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public Saving getSaving(){
        Saving saving = new Saving(this);
        return saving;
    }

    public GameStatePojo getGameState(){
        GameStatePojo gameStatePojo = new GameStatePojo();
        gameStatePojo.setCurrentPhase(this.currentPhase instanceof ActionPhase ? Phase.ACTION : Phase.PIANIFICATION);
        gameStatePojo.setGameId(this.getGameID());

        if (this.winner != null){
            gameStatePojo.setWinner(this.winner.getNickname());
        }
        gameStatePojo.setGameOver(this.gameOver);
        gameStatePojo.setExpert(this.expert);

        if (this.currentPlayer != null){
            PlayerPojo pojoCurrentPlayerPojo = currentPlayer.toPojo();
            gameStatePojo.setCurrentPlayer(pojoCurrentPlayerPojo);
        }

        List<PlayerPojo> pojoPlayerPojos = new ArrayList<>();
        for (Player p: this.game.getPlayers()){
            PlayerPojo pojoPlayerPojo = p.toPojo();
            pojoPlayerPojos.add(pojoPlayerPojo);
        }
        gameStatePojo.setPlayers(pojoPlayerPojos);

        PawnsMapPojo pojoProfessorsLeft = new PawnsMapPojo(this.game.getProfessorsLeft());
        gameStatePojo.setProfessorsLeft(pojoProfessorsLeft);

        PawnsMapPojo pojoStudentsBag = new PawnsMapPojo(this.game.getStudentsBag());
        gameStatePojo.setStudentsBag(pojoStudentsBag);

        gameStatePojo.setCoinSupply(this.game.getCoinSupply());

        List<CharacterPojo> pojoCharacterPojos = new ArrayList<>();
        for (CharacterState c : this.game.getCharacters()){
            CharacterPojo pojoCharacterPojo = c.toPojo();
            pojoCharacterPojos.add(pojoCharacterPojo);
        }
        gameStatePojo.setCharacters(pojoCharacterPojos);

        if (this.game.getActiveEffect() != null){
            CharacterPojo pojoActiveCharacterPojo = this.game.getActiveEffect().toPojo();
            gameStatePojo.setActiveEffect(pojoActiveCharacterPojo);
        }else{
            gameStatePojo.setActiveEffect(null);
        }

        List<IslandPojo> pojoIslandPojos = new ArrayList<>();
        for(Island i: game.getIslands()){
            IslandPojo pojoIslandPojo = i.toPojo();
            pojoIslandPojos.add(pojoIslandPojo);
        }
        gameStatePojo.setIslands(pojoIslandPojos);

        List<CloudPojo> pojoCloudPojos = new ArrayList<>();
        for(Cloud c: game.getClouds()){
            CloudPojo pojoCloudPojo = c.toPojo();
            pojoCloudPojos.add(pojoCloudPojo);
        }
        gameStatePojo.setClouds(pojoCloudPojos);

        return gameStatePojo;
    }

    public Lobby getLobby() {
        return lobby;
    }
    public void setForceStop(boolean forceStop) {
        this.forceStop = forceStop;
    }

    public void setActionPhase(ActionPhase actionPhase) {
        this.actionPhase = actionPhase;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isForceStop() {
        return forceStop;
    }

    public Player getWinner() {
        return winner;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public void setCharacterEffects(List<CharacterEffect> characterEffects) {
        this.characterEffects = characterEffects;
    }

    public PianificationPhase getPianificationPhase() {
        return pianificationPhase;
    }

    public void setPianificationPhase(PianificationPhase pianificationPhase) {
        this.pianificationPhase = pianificationPhase;
    }

    public boolean isLastRoundFinishedAssistantCards() {
        return isLastRoundFinishedAssistantCards;
    }

    public void setLastRoundFinishedAssistantCards(boolean lastRoundFinishedAssistantCards) {
        isLastRoundFinishedAssistantCards = lastRoundFinishedAssistantCards;
    }

    public boolean isLastRoundFinishedStudentsBag() {
        return isLastRoundFinishedStudentsBag;
    }

    public void setLastRoundFinishedStudentsBag(boolean lastRoundFinishedStudentsBag) {
        isLastRoundFinishedStudentsBag = lastRoundFinishedStudentsBag;
    }

    public boolean isFinishedTowers() {
        return isFinishedTowers;
    }

    public void setFinishedTowers(boolean finishedTowers) {
        isFinishedTowers = finishedTowers;
    }

    public boolean isThreeOrLessIslands() {
        return isThreeOrLessIslands;
    }

    public void setThreeOrLessIslands(boolean threeOrLessIslands) {
        isThreeOrLessIslands = threeOrLessIslands;
    }

    public PianificationResult getPianificationResult() {
        return pianificationResult;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void save(){

        try {
            String currentPath = new File(".").getCanonicalPath();
            System.out.println(currentPath);
            String fileName = currentPath + "/src/main/Resources/savings/Game" + gameID + ".txt";
            File file = new File(fileName);
            file.createNewFile();
            String dataString = JsonConverter.fromSavingToJson(this.getSaving());
            FileOutputStream outputStream = new FileOutputStream(file, false);
            byte[] strToBytes = dataString.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e ){
            e.printStackTrace();
        }

    }
}

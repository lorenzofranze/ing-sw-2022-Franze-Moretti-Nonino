package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.ColourTower;
import it.polimi.ingsw.server.controller.characters.Card5Effect;
import it.polimi.ingsw.server.controller.characters.CharacterEffect;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.CharacterState;

import java.util.*;


public class ActionPhase extends GamePhase {
    private final GameController gameController;

    private HashMap<Player, Integer> maximumMovements;
    private List<Player> turnOrder;
    private ActionResult actionResult;

    public ActionPhase(GameController gameController) {
        this.gameController = gameController;
        maximumMovements = null;
        turnOrder = null;
        actionResult = new ActionResult();
    }

    public ActionResult handle(List<Player> turnOrder, HashMap<Player, Integer> maximumMovements,
                               boolean isLastRoundFinishedStudentsBag) {

        MessageHandler messageHandler = this.gameController.getMessageHandler();
        this.maximumMovements = maximumMovements;
        this.turnOrder = turnOrder;
        boolean isEnded = false;
        actionResult.setFirstPianificationPlayer(turnOrder.get(0));

        for (Player p : turnOrder) {

            gameController.setCurrentPlayer(p);
            gameController.update();

            if (!(actionResult.isFinishedTowers() || actionResult.isThreeOrLessIslands())){

                //move students (while moving the students, the player can decide to play a characterCard)
                int studentsToMove = gameController.getGame().getPlayers().size()+1;

                askforCharacter();
                if (checkEnd() == true){return actionResult;}

                moveStudents();
                gameController.update();

                if (checkEnd() == true){return actionResult;}

                //move mother nature
                Island whereMotherNature = moveMotherNature(p);
                Player moreInfluentPlayer = calcultateInfluence(whereMotherNature);
                if(whereMotherNature.getNumNoEntryTile()>0){
                    whereMotherNature.setNumNoEntryTile(whereMotherNature.getNumNoEntryTile()-1);
                    for(CharacterState c : gameController.getGame().getCharacters())
                        if(c.getCharacterId()==5) {
                            ((Card5Effect)gameController.getCharacterEffects().get(c)).addNoEntryTile();
                        }
                }

                gameController.update();

                if (moreInfluentPlayer != null){
                    isEnded = placeTowerOfPlayer(moreInfluentPlayer, whereMotherNature);
                    if (isEnded) {
                        actionResult.setFinishedTowers(true);
                        return actionResult;
                    }
                    boolean union = verifyUnion();
                    int numIslands= this.gameController.getGame().getIslands().size();
                    if(numIslands<4){
                        actionResult.setThreeOrLessIslands(true);
                        return actionResult;
                    }
                }

                /*gameController.update();

                System.out.println("\nACTION PHASE - HANDLE - FLAG 1\n");
                if (true ) return null;
                gameController.update();*/
            }

            if (checkEnd() == true){return actionResult;}

            askforCharacter();

            if (checkEnd() == true){return actionResult;}
            /*in this round players choose the cloud only if in the pianification phase i had enough
            studentsPawns in the bag to fill ALL the clouds*/

            gameController.update();

            if (!isLastRoundFinishedStudentsBag) {
                System.out.println("\nACTION PHASE - HANDLE - FLAG 1\n");
                chooseCloud();
                System.out.println("\nACTION PHASE - HANDLE - FLAG 2\n");
                gameController.update();
                System.out.println("\nACTION PHASE - HANDLE - FLAG 3\n");
            }
            System.out.println("\nACTION PHASE - HANDLE - FLAG 4\n");

            if (checkEnd() == true){return actionResult;}

        }
        /*reset characterEffects activated*/
        gameController.getGame().setActiveEffect(null);

        return actionResult;
    }


    /** if where==-1 moves student from the schoolboard entrance to the diningroom
     * else moves student from the schoolboard entrance to the island of index=where
     * @param colour
     * @param where
     */
    public void moveSingleStudent(ColourPawn colour, Integer where){
        int coinsToAdd;
        PawnsMap student= new PawnsMap();
        student.add(colour);

        if (where == -1) {
            coinsToAdd = gameController.getCurrentPlayer().getSchoolBoard().fromEntranceToDiningRoom(student, gameController.getGame());
            gameController.getCurrentPlayer().addCoins(gameController.isExpert() ? coinsToAdd : 0);
        } else {
            gameController.getCurrentPlayer().getSchoolBoard().getEntrance().remove(colour);
            gameController.getGame().getIslands().get(where).addStudents(student);
        }
    }


    protected void moveStudents(){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        Message receivedMessage;
        Message errorGameMessage;
        boolean valid=true;
        int indexColour;
        PawnMovementMessage gameMessage;
        int where = 0;   // -1 refer for diningRoom, index of island for island

        int studentsToMove = 0;
        if (gameController.getGame().getPlayers().size() == 2){studentsToMove = 3;}
        if (gameController.getGame().getPlayers().size() == 3){studentsToMove = 4;}

        for(int i=0; i<studentsToMove; i++){

            do{
                valid = true;
                receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.PawnMovement);
                if (receivedMessage == null){
                    System.out.println("ERROR-moveStudent");
                    return;
                }
                gameMessage = (PawnMovementMessage) receivedMessage;
                indexColour = gameMessage.getColour();
                if(indexColour<=-1 || indexColour>=5){
                    valid=false;
                    errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice); // index colour invalid
                    playerManager.sendMessage(errorGameMessage);
                }
                if(valid){
                    if (gameController.getCurrentPlayer().getSchoolBoard()
                            .getEntrance().get(ColourPawn.get(indexColour)) <= 0){
                        valid = false;
                        errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);  // no student
                        playerManager.sendMessage(errorGameMessage);
                    }
                }
                if(valid){
                    where = gameMessage.getWhere();
                    if(where!= -1 && (where <0 || where > gameController.getGame().getIslands().size()-1 )) {
                        valid = false;
                        errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice); // destination not valid
                        playerManager.sendMessage(errorGameMessage);
                    }
                }
                if(valid && gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().
                        get(ColourPawn.get(indexColour))>=10) {
                    valid = false;
                    errorGameMessage=new ErrorMessage(TypeOfError.FullDiningRoom); // out of row -> 10 students
                    playerManager.sendMessage(errorGameMessage);
                }
            }while(!valid);
            AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
            playerManager.sendMessage(ackMessage);
            this.moveSingleStudent(ColourPawn.get(indexColour), where);
            gameController.update();
            askforCharacter();
        }

    }

    protected Island moveMotherNature(Player currentPlayer){
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        Message receivedMessage;
        GameMessage gameMessage;
        Message errorGameMessage;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        PlayerManager playerManager=messageHandler.getPlayerManagerMap().get(currPlayer);
        Island ris = null;
        int played;

        boolean valid = false;
        do {
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.MoveMotherNature);
            if (receivedMessage == null){
                System.out.println("ERROR-moveStudent");
                return null;
            }
            gameMessage = (GameMessage) receivedMessage;
            played = gameMessage.getValue();
            if (played < 1 || played > maximumMovements.get(currentPlayer)){
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice); // steps not valid
                playerManager.sendMessage(errorGameMessage);
            }else{
                valid = true;
            }
        } while(valid == false);

        AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
        playerManager.sendMessage(ackMessage);

        List<Island> islandList = this.gameController.getGame().getIslands();

        boolean flag = false; //used to find the current position of motherNature
        for(int i = 0; i < islandList.size() && flag == false; i++){
            if (islandList.get(i).getHasMotherNature() == true){
                flag = true;
                islandList.get(i).setHasMotherNature(false);
                islandList.get((i+played) % islandList.size()).setHasMotherNature(true);

                ris = islandList.get((i+played) % islandList.size());
            }
        }
        return ris;
    }


    /**if some particualr characters are active it's not called the usual method: island.getInfluence() but
     * it's called the getInfluence() method of that character: this method returns the more influence player
     * according to the new effect (e.g. towers are not counted)
     */
    public Player calcultateInfluence(Island island){
        if(island.getNumNoEntryTile() >0){
            return null;
        }
        /* if some particualr characters are active it's not called the usual method: island.getInfluence() but
        * it's called the getInfluence() method of that character: this method returns the more influence player
        * according to the new effect (e.g. towers are not counted)
         */
        Player moreInfluentPlayer;
        if(gameController.getGame().getActiveEffect()!=null) {
            int characterId=gameController.getGame().getActiveEffect().getCharacterId();
            if(characterId==2 || characterId==6 || characterId==8 || characterId==9){
                CharacterEffect character= gameController.getCharacterByID(characterId);
                moreInfluentPlayer = character.effectInfluence(island);
                return moreInfluentPlayer;
            }
        }
        // if none of previous character effects is active, influence is as usual... :
        moreInfluentPlayer = island.getInfluence(gameController.getGame());
        return moreInfluentPlayer;
    }

    /**places the tower of the player on the island. Returns true if one player has finished his towers*/
    public boolean placeTowerOfPlayer(Player moreInfluentPlayer, Island island){
        ColourTower color=moreInfluentPlayer.getColourTower();
        if(island.getTowerCount()==0){
            island.addTower(1);
            moreInfluentPlayer.getSchoolBoard().removeTower();
            island.setTowerColor(color);
            if (moreInfluentPlayer.getSchoolBoard().getSpareTowers() == 0) {return true;}
        } else {
            if (!island.getTowerColour().equals(color)) {

                /*adding the towers on the schoolboard of the player who has lost the island control*/
                Player oldOwner = null;
                for (Player player : gameController.getGame().getPlayers()) {
                    if (player.getColourTower() == island.getTowerColour()) {
                        oldOwner = player;
                    }
                }
                oldOwner.getSchoolBoard().addTower(island.getTowerCount());

                /*removing the towers on the schoolboard of the player who won the island control*/
                int numLeftTowers = moreInfluentPlayer.getSchoolBoard().getSpareTowers();
                moreInfluentPlayer.getSchoolBoard().removeTower(island.getTowerCount());
                island.setTowerColor(color);
                if (numLeftTowers < island.getTowerCount()) {return true;}
            }
        }
        return false;
    }

    /**returns true if there was a union, false otherwise*/
    public boolean verifyUnion() {
        List<Island> islandList = this.gameController.getGame().getIslands();
        List<Integer> currColour;
        HashMap<ColourTower, List<Integer>> colourMap = new HashMap<ColourTower, List<Integer>>();

        for(ColourTower c : ColourTower.values()){
            currColour = new ArrayList<>();
            colourMap.put(c, currColour);
        }
        for(int i = 0; i < islandList.size(); i++){
            if (islandList.get(i).getTowerCount() > 0){
                colourMap.get(islandList.get(i).getTowerColour()).add(i);
            }
        }

        /*at this point colourMap is a map where the key is a ColourTower and the value contained is a List of Integer
        corrisponding at the index of the isalnds where the tower has that colour*/

        boolean flag = false; //true when islands of the same colour are adjacent
        Set<Integer> toUnify = new HashSet<>();//set of the islands index to unify

        for(ColourTower c : ColourTower.values()) {
            currColour = colourMap.get(c);

            for(Integer i : currColour){
                for(Integer j : currColour){
                    if (j.equals((i+1) % islandList.size())){
                        toUnify.add(i);
                        toUnify.add(j);
                        flag = true;
                    }
                }
            }
        }

        List<Island> ris = new ArrayList<>(); //list of the islands to unify
        if (flag == true){
            for(Integer i : toUnify){
                ris.add(islandList.get(i));
            }
        }

        if (ris.size() == 0) {return false;}

        this.gameController.getGame().unifyIslands(ris);

        return true;
    }

    protected void chooseCloud(){
        Message receivedMessage;
        GameMessage gameMessage;
        Message errorGameMessage;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer=gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager = messageHandler.getPlayerManager(currPlayer);
        int indexCloud;

        boolean valid = false;
        do{
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.CloudChoice);
            if (receivedMessage == null){
                System.out.println("ERROR-chooseCloud");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            indexCloud= gameMessage.getValue();
            if(indexCloud<0 || indexCloud > gameController.getGame().getPlayers().size()-1){
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice); //index not valid
                playerManager.sendMessage(errorGameMessage);
            }else{
                if(gameController.getGame().getClouds().get(indexCloud).getStudents().isEmpty()){
                    errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice); //empty cloud, rechoose
                    playerManager.sendMessage(errorGameMessage);
                    valid=false;
                }else{
                    AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
                    playerManager.sendMessage(ackMessage);
                    valid = true;
                }
            }
        }while(!valid);

        gameController.getCurrentPlayer().getSchoolBoard().insertCloud(gameController.getGame().getClouds().get(indexCloud));
    }

    // fine metodi di gioco

    /** this method does nothing if game is in simple mode because no player has more than 0 coins
     * otherwise it asks to the player for character card he wants to use between that he can afford */
    protected void askforCharacter(){
        GameMessage gameMessage;
        ErrorMessage errorGameMessage;
        Message receivedMessage;
        Integer cardPlayed = null;
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        PlayerManager playerManager = messageHandler.getPlayerManagerMap().get(currPlayer);

        int cardNumber;
        boolean validChoice = false;

        do{
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.CharacterCard);
            gameMessage = (GameMessage) receivedMessage;

            if (gameMessage.getValue() == null){
                //il giocatore NON ha voluto giocare una carta personaggio
                AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
                playerManager.sendMessage(ackMessage);
                validChoice = true;
                return;
            }

            Integer playedCard = gameMessage.getValue();
            CharacterState characterStatePlayed = null;
            boolean cardExists = false;
            for(CharacterState characterState : gameController.getGame().getCharacters()) {
                if (characterState.getCharacterId() == playedCard) {
                    characterStatePlayed = characterState;
                    cardExists = true;
                }
            }


            if (cardExists){
                if (gameController.getCurrentPlayer().getCoins() < characterStatePlayed.getCost()) {
                    ErrorMessage errorMessage = new ErrorMessage(TypeOfError.NoMoney);
                    playerManager.sendMessage(errorMessage);
                } else {

                    gameController.getGame().setActiveEffect(characterStatePlayed);
                    gameController.getCurrentPlayer().removeCoins(characterStatePlayed.getCost());
                    gameController.getGame().addCoins(characterStatePlayed.getCost());

                    characterStatePlayed.use(); //incremento il costo se è da incrementare

                    AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
                    playerManager.sendMessage(ackMessage);

                    gameController.update();


                    CharacterEffect currentCharacterEffect = gameController.getCharacterByID(characterStatePlayed.getCharacterId());
                    currentCharacterEffect.doEffect();
                    validChoice = true;
                }
            }else{
                ErrorMessage errorMessage = new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorMessage);
            }
        }while(validChoice == false);
    }

    public void setCurrPlayer(Player currPlayer) {
        this.gameController.setCurrentPlayer(currPlayer);
    }

    public void setMaximumMovements(HashMap<Player, Integer> maximumMovements) {
        this.maximumMovements = maximumMovements;
    }

    public void setTurnOrder(List<Player> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    /**returns true if a player has finished his towers / there are less than 4 islands*/
    public boolean checkEnd(){
        if (actionResult.isFinishedTowers() == true) return true;
        if (actionResult.isThreeOrLessIslands() == true) return true;
        return false;
    }

    @Override
    public String toString(){
        return "ActionPhase";
    }

    
}

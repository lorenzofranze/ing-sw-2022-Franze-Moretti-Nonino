package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exception.EndGameException;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Character;
import it.polimi.ingsw.Controller.Characters.*;
import java.util.*;
import java.util.stream.Collectors;

public class ActionPhase implements GamePhase {

    private GameController gameController;
    private HashMap<Player, Integer> maximumMovements;
    private List<Player> turnOrder;
    private Map<Character, CharacterEffect> characterEffects; // per gli effetti

    public ActionPhase(GameController gc) {
        this.gameController = gc;

        this.maximumMovements = new HashMap<Player, Integer>();
        List<Player> list = gc.getGame().getPlayers();
        for(Player p : list){
            maximumMovements.put(p, 0);
        }
        turnOrder = new ArrayList<>(list.size());

        this.initializeCharactersEffects();
    }
    // da vedere

    private void initializeCharactersEffects(){

        for(Character cr: gameController.getGame().getCharacters()){
            if(cr.getCharacterId()==1)
                characterEffects.put(cr, new Card1());
                // e così via...
                //....

            if(cr.getCharacterId()==12)
            characterEffects.put(cr, new Card12());
        }
    }




    //RICORDARE: alla fine del turno di ogni player resettare activeEffect di game

    @Override
    public void handle() {

        MessageHandler messageHandler = this.gameController.getMessageHandler();

        try {
            for (Player p : turnOrder) {
                moveStudents();
                moveMotherNature(p);

                chooseCloud();
            }
        }catch (EndGameException exception){
            exception.printStackTrace();
        }

        computePlayerPianification();
        this.gameController.setGamePhase(gameController.getPianificationPhase());
    }

    private void computePlayerPianification () {
        Player firstInPianification = turnOrder.get(0);
        PianificationPhase p = (PianificationPhase) this.gameController.getPianificationPhase();
        p.setFirstPlayer(firstInPianification);
    }

    /** if where==-1 moves student from the schoolboard entrance to the diningroom
     * else moves student from the schoolboard entrance to the island of index=where
     * @param colour
     * @param where
     */
    private void moveSingleStudent(ColourPawn colour, Integer where){
        int coinsToAdd;
        PawnsMap student= new PawnsMap();
        student.add(colour);

        if (where == -1) {
            coinsToAdd = gameController.getCurrentPlayer().getSchoolBoard().fromEntranceToDiningRoom(student, gameController.getGame());
            // operatore ternario XD
            gameController.getCurrentPlayer().addCoins(gameController.isExpert() ? coinsToAdd : 0);
        } else {
            gameController.getCurrentPlayer().getSchoolBoard().getEntrance().remove(colour);
            gameController.getGame().getIslands().get(where).addStudents(student);
        }

    }


    private void moveStudents(){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        boolean valid=true;
        int indexColour;
        int where = 0;   // -1 refer for diningRoom, index of island for island

        for(int i=0; i<3; i++){
            // to user: choose your i+1 movement of 3
            do{
                valid = true;
                // to user: choose one color pawn
                indexColour = messageHandler.getValueCLI("choose one color pawn",gameController.getCurrentPlayer());
                if(indexColour<=-1 || indexColour >=5){
                    valid=false;
                    // to user: index not valid
                }
                if(valid){
                    if(! (gameController.getGame().getCurrentPlayer().getSchoolBoard()
                            .getEntrance().get(ColourPawn.values()[indexColour]) >=1)){
                        valid = false;
                        //to user: change color pawn to move, you don't have that color
                    }
                }

                // to user: choose position

                if(valid){
                    where = messageHandler.getValueCLI("choose position", gameController.getCurrentPlayer());
                    if(where!= -1 && (where <0 || where > gameController.getGame().getIslands().size()-1 )) {
                        valid = false;
                        //to user: position not valid
                    }
                }
                if(valid && gameController.getGame().getCurrentPlayer().getSchoolBoard().getDiningRoom().
                        get(ColourPawn.values()[indexColour])==10 ){
                    valid = false;
                    // to user: your school board in that row of your dining room is full
                }
            }while(!valid);

            // to user: ok
            this.moveSingleStudent(ColourPawn.values()[indexColour], where );

        }
    }

    private void moveMotherNature(Player currentPlayer) throws EndGameException{
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int played;

        do{
            played = messageHandler.getValue(currentPlayer);
        }
        while(played < 1 || played > maximumMovements.get(currentPlayer));

        List<Island> islandList = this.gameController.getGame().getIslands();

        boolean flag = false;
        for(int i = 0; i < islandList.size() && flag == false; i++){
            if (islandList.get(i).getHasMotherNature() == true){
                flag = true;
                islandList.get(i).setHasMotherNature(false);
                islandList.get(i+played).setHasMotherNature(true);

                calcultateInfluence(islandList.get(i+played));
            }
        }
    }


    public void calcultateInfluence(Island island) throws EndGameException{
        if(island.getHasNoEntryTile()){
            island.setHasNoEntryTile(false);
            return;
        }
        Player moreInfluentPlayer = island.getInfluence(gameController.getGame());
        this.placeTowerOfPlayer(moreInfluentPlayer, island );

    }

    /**places a tower on a island or swap the color, then, if necessary, calls verifyUnion()
     * @param colour
     * @param island
     */
    public void placeTowerOfPlayer(Player moreInfluentPlayer, Island island) throws EndGameException {
        ColourTower color=moreInfluentPlayer.getColourTower();
        if(island.getTowerCount()==0){
            island.addTower(1);
            island.setTowerColor(color);
            verifyUnion();
        }
        if(!island.getTowerColour().equals(color)){
            int numLeftTowers=moreInfluentPlayer.getSchoolBoard().getSpareTowers();
            if(numLeftTowers<island.getTowerCount()){
                gameController.setGameOver(true);
                gameController.getMessageHandler().setWinner(gameController.getCurrentPlayer());
                throw new EndGameException();
            }
            island.setTowerColor(color);
            verifyUnion();
        }
    }

    private void verifyUnion() throws EndGameException{
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

        this.gameController.getGame().unifyIslands(ris);
        int numIslands= this.gameController.getGame().getIslands().size();
        if(numIslands<4){
            throw new EndGameException();
        }
    }

    private void chooseCloud(){
        boolean valid;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int indexCloud;
        // if there is only one cloud left, it goes directly in player's schoolBoard
        List<Cloud> cloudNotEmpty;
        cloudNotEmpty = (gameController.getGame().getClouds()).stream()
                .filter(x -> ! x.getStudents().isEmpty()).collect(Collectors.toList());
        if(  cloudNotEmpty.size() == 1 ){
            gameController.getCurrentPlayer().getSchoolBoard().insertCloud(cloudNotEmpty.get(0));
            return;
        }
        // to user: choose one cloud
        // print possibile cloud with values
        do{
            valid = true;
            indexCloud = messageHandler.getValueCLI("choose one cloud",gameController.getCurrentPlayer());
            if(indexCloud<0 || indexCloud > gameController.getGame().getPlayers().size()-1){
                // to user: index not valid
                valid = false;
            }
            if(valid){
                if(gameController.getGame().getClouds().get(indexCloud).getStudents().isEmpty()){
                    // to user: empty cloud, rechoose
                    valid=false;
                }
            }
        }while(!valid);
        // to user: ok
        gameController.getCurrentPlayer().getSchoolBoard().insertCloud(gameController.getGame().getClouds().get(indexCloud));
    }

    // fine metodi di gioco

    /** this method does nothing if game is in simple mode because no player has more than 0 coins
     * otherwise it ask the player for character card he wants to use between that he can afford */
    private void askforCharacter(){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int cardNumber;

        List<Character> usable = new ArrayList<>();
        for(Character character: gameController.getGame().getCharacters()) {
            if (gameController.getGame().getCurrentPlayer().getCoins() >= character.getCost() &&
                    gameController.getGame().getActiveEffect() == null) {
                usable.add(character);
            }
        }

        if(!usable.isEmpty()){
            // to user: you can play one of theese cards.. select the number of card you want to
            // use to play the card now, any other key to skip

            for(Character character : usable) {
                // to user: choose one of theese cards...print...
            }
            cardNumber = messageHandler.getValueCLI("choose one of theese cards", gameController.getCurrentPlayer());
            if(cardNumber >= 0 && cardNumber<=usable.size()-1){
                gameController.getGame().setActiveEffect(usable.get(cardNumber));
                gameController.getCurrentPlayer().removeCoins(usable.get(cardNumber).getCost());
                gameController.getGame().addCoins(usable.get(cardNumber).getCost());

                for(Character cr: gameController.getGame().getCharacters())
                    if(cr.getCharacterId() == usable.get(cardNumber).getCharacterId()){
                        cr.use();
                    }

                CharacterEffect currentCharacterEffect= characterEffects.get(usable.get(cardNumber));
                currentCharacterEffect.doEffect();

            }
        }

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

}

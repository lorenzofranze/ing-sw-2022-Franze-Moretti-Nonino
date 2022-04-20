package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Controller.Characters.Card5;
import it.polimi.ingsw.Server.Controller.Characters.CharacterEffect;
import it.polimi.ingsw.Server.Controller.Characters.CharacterEffectInfluence;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Character;

import java.util.*;
import java.util.stream.Collectors;

public class ActionPhase extends GamePhase {
    private final GameController gameController;

    private HashMap<Player, Integer> maximumMovements;
    private List<Player> turnOrder;





    public ActionPhase(GameController gameController) {
        this.gameController = gameController;

        maximumMovements = null;
        turnOrder = null;

    }




    //RICORDARE: alla fine del turno di ogni player resettare activeEffect di game
    public ActionResult handle(List<Player> turnOrder, HashMap<Player, Integer> maximumMovements,
                               boolean isLastRoundFinishedStudentsBag) {

        MessageHandler messageHandler = this.gameController.getMessageHandler();

        this.maximumMovements = maximumMovements;
        this.turnOrder = turnOrder;
        boolean isEnded = false;

        ActionResult actionResult = new ActionResult();
        actionResult.setFirstPianificationPlayer(turnOrder.get(0));


        for (Player p : turnOrder) {
            if (!(actionResult.isFinishedTowers() || actionResult.isThreeOrLessIslands())){

                gameController.setCurrentPlayer(p);

                System.out.println("\t\t\t\t\t\t\t\t\t\t\t" + gameController.getCurrentPlayer().toString().toUpperCase(Locale.ROOT) + " TURN \n");

                for (int i = 0; i < gameController.getGame().getPlayers().size(); i++){
                    System.out.println("\n\t\t\t\t" + gameController.getGame().getPlayers().get(i).toString().toUpperCase(Locale.ROOT) + " SCHOOLBOARD");
                    System.out.println("ENTRANCE:" + gameController.getGame().getPlayers().get(i).getSchoolBoard().getEntrance().toString());
                    System.out.println("DINING ROOM:"+ gameController.getGame().getPlayers().get(i).getSchoolBoard().getDiningRoom().toString());
                    System.out.println("PROFESSORS:" + gameController.getGame().getPlayers().get(i).getSchoolBoard().getProfessors().toString());
                }

                System.out.println("\nMOTHERNATURE: Island number " + gameController.getGame().findMotherNature());
                System.out.println("\nISLANDS:\n" + gameController.getGame().islandsToString());

                askforCharacter();

                moveStudents();

                System.out.println("\n\t\t\t\t\tNEW PROFESSORS DISTRIBUTION");
                for (int i = 0; i < gameController.getGame().getPlayers().size(); i++){
                    System.out.print(gameController.getGame().getPlayers().get(i).toString().toUpperCase(Locale.ROOT) + " PROFESSORS: ");
                    System.out.print(gameController.getGame().getPlayers().get(i).getSchoolBoard().getProfessors().toString() + "\n");
                }

                askforCharacter();

                System.out.print("\nMOTHERNATURE: Island number " + gameController.getGame().findMotherNature());
                System.out.println(" (maximumMovements for mothernature: " + maximumMovements.get(gameController.getCurrentPlayer()) + ")\n");

                Island whereMotherNature = moveMotherNature(p);

                System.out.println("\nMOTHERNATURE: moved to Island number " + gameController.getGame().findMotherNature());

                if (whereMotherNature.getHasNoEntryTile() == true){
                    Card5 card5 = Card5.getInstance(gameController);
                    card5.addNoEntryTile();
                    whereMotherNature.setHasNoEntryTile(false);
                }else{

                    Player moreInfluentPlayer = calcultateInfluence(whereMotherNature);

                    if (moreInfluentPlayer == null){
                        System.out.println("MOREINFLUENTPLAYER: none");
                    } else {
                        System.out.println("MOREINFLUENTPLAYER: "+ moreInfluentPlayer.toString());
                    }


                    if (moreInfluentPlayer != null){
                        //isEnded is true if one player has finished his towers
                        isEnded = placeTowerOfPlayer(moreInfluentPlayer, whereMotherNature);
                        if (isEnded) {
                            actionResult.setFinishedTowers(true);
                            System.out.println(gameController.getCurrentPlayer().toString() + " has finished his/her Towers");
                            return actionResult;
                        }

                        //union is true if there was a union
                        boolean union = verifyUnion();

                        //isEnded is true if there are only 3 or less islands
                        isEnded = verifyUnion();

                        int numIslands= this.gameController.getGame().getIslands().size();

                        if(numIslands<4){
                            actionResult.setThreeOrLessIslands(true);
                            System.out.println("There are 3 or less islands");
                            return actionResult;
                        }

                    }
                }

                if(!(actionResult.isFinishedTowers() || actionResult.isThreeOrLessIslands())) {

                    askforCharacter();

                    if (!isLastRoundFinishedStudentsBag) {
                        System.out.println("\nCLOUDS:\n" + gameController.getGame().cloudsToString());
                        chooseCloud();
                    }
                    askforCharacter();
                }
            }

            // reset characterEffects activated
            gameController.getGame().setActiveEffect(null);

        }

        return actionResult;

    }


    /** if where==-1 moves student from the schoolboard entrance to the diningroom
     * else moves student from the schoolboard entrance to the island of index=where
     * @param colour
     * @param where
     */
    protected void moveSingleStudent(ColourPawn colour, Integer where){
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


    protected void moveStudents(){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        boolean valid=true;
        int indexColour;
        int where = 0;   // -1 refer for diningRoom, index of island for island

        int studentsToMove = 0;
        if (gameController.getGame().getPlayers().size() == 2){studentsToMove = 3;}
        if (gameController.getGame().getPlayers().size() == 3){studentsToMove = 4;}

        for(int i=0; i<studentsToMove; i++){
            // to user: choose your i+1 movement of 3
            do{
                valid = true;
                // to user: choose one color pawn

                indexColour = messageHandler.getValueCLI("choose one color pawn: ",gameController.getCurrentPlayer());
                if(indexColour<=-1 || indexColour >=5){
                    valid=false;
                    // to user: index not valid
                    System.out.println("indexColour not valid.");
                }
                if(valid){
                    if (gameController.getCurrentPlayer().getSchoolBoard()
                            .getEntrance().get(ColourPawn.get(indexColour)) <= 0){
                        valid = false;
                        //to user: change color pawn to move, you don't have that color
                        System.out.println("You don't have that colour.");
                    }
                }

                // to user: choose position

                if(valid){
                    where = messageHandler.getValueCLI("choose position (insert island index or '-1' to place the student in the dining room): ", gameController.getCurrentPlayer());
                    if(where!= -1 && (where <0 || where > gameController.getGame().getIslands().size()-1 )) {
                        valid = false;
                        //to user: position not valid
                    }
                }
                if(valid && gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().
                        get(ColourPawn.get(indexColour))>=10) {
                    valid = false;
                    // to user: your school board in that row of your dining room is full
                }
            }while(!valid);

            // to user: ok
            this.moveSingleStudent(ColourPawn.get(indexColour), where );

        }
    }

    protected Island moveMotherNature(Player currentPlayer){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        Island ris = null;
        int played;

        do{
            played = messageHandler.getValueCLI("choose how many steps should do MotherNature: ", gameController.getCurrentPlayer());
        }
        while(played < 1 || played > maximumMovements.get(currentPlayer));


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


    public Player calcultateInfluence(Island island){
        if(island.getHasNoEntryTile()){
            island.setHasNoEntryTile(false);
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
                CharacterEffectInfluence character= (CharacterEffectInfluence) gameController.getCharacterEffects().get(gameController.getGame().getActiveEffect());
                moreInfluentPlayer = character.effectInfluence(island);
                return moreInfluentPlayer;
            }
        }
        // if none of previous character effects is active, influence is as usual... :
        moreInfluentPlayer = island.getInfluence(gameController.getGame());
        return moreInfluentPlayer;
    }

    /**places a tower on a island or swap the color, then, if necessary, calls verifyUnion()
     * @param colour
     * @param island
     */
    public boolean placeTowerOfPlayer(Player moreInfluentPlayer, Island island){
        ColourTower color=moreInfluentPlayer.getColourTower();
        if(island.getTowerCount()==0){
            island.addTower(1);
            moreInfluentPlayer.getSchoolBoard().removeTower();
            island.setTowerColor(color);
            if (moreInfluentPlayer.getSchoolBoard().getSpareTowers() == 0) {return true;}
        } else {
            if (!island.getTowerColour().equals(color)) {

                //RIAGGIUNGO LE TORRI ALLA SCHOOLBOARD DEL GIOCATOIRE CHE HA "PERSO" IL POSSESSO DELL'ISOLA
                Player oldOwner = null;
                for (Player player : turnOrder) {
                    if (player.getColourTower() == island.getTowerColour()) {
                        oldOwner = player;
                    }
                }
                oldOwner.getSchoolBoard().addTower(island.getTowerCount());

                //TOLGO LE TORRI ALLA SCHOOLBOARD DEL GIOCATORE CHE HA "VINTO" IL POSSESSO DELL'ISOLA
                int numLeftTowers = moreInfluentPlayer.getSchoolBoard().getSpareTowers();
                moreInfluentPlayer.getSchoolBoard().removeTower(island.getTowerCount());
                island.setTowerColor(color);
                if (numLeftTowers < island.getTowerCount()) {return true;}
            }
        }
        return false;
    }

    protected boolean verifyUnion() {
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
        boolean valid;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int indexCloud;
        // if there is only one cloud left, it goes directly in player's schoolBoard
        List<Cloud> cloudNotEmpty;
        cloudNotEmpty = (gameController.getGame().getClouds()).stream()
                .filter(x -> ! x.getStudents().isEmpty()).collect(Collectors.toList());
        if(  cloudNotEmpty.size() == 1 ){
            gameController.getCurrentPlayer().getSchoolBoard().insertCloud(cloudNotEmpty.get(0));

            System.out.println("There is only one Cloud left. You have received students form Cloud number " +
                    gameController.getGame().getClouds().indexOf(cloudNotEmpty.get(0)));

            return;
        }
        // to user: choose one cloud
        // print possibile cloud with values
        do{
            valid = true;
            indexCloud = messageHandler.getValueCLI("choose one cloud: ",gameController.getCurrentPlayer());
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
    protected void askforCharacter(){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int cardNumber;

        List<Character> usable = new ArrayList<>();
        for(Character character: gameController.getGame().getCharacters()) {
            if (gameController.getCurrentPlayer().getCoins() >= character.getCost() &&
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
            cardNumber = messageHandler.getValueCLI("choose one of theese cards: ", gameController.getCurrentPlayer());
            if(cardNumber >= 0 && cardNumber<=usable.size()-1) {
                gameController.getGame().setActiveEffect(usable.get(cardNumber));
                gameController.getCurrentPlayer().removeCoins(usable.get(cardNumber).getCost());
                gameController.getGame().addCoins(usable.get(cardNumber).getCost());

                for (Character cr : gameController.getGame().getCharacters())
                    if (cr.getCharacterId() == usable.get(cardNumber).getCharacterId()) {
                        cr.use();
                    }

                CharacterEffect currentCharacterEffect = gameController.getCharacterEffects().get(usable.get(cardNumber));
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

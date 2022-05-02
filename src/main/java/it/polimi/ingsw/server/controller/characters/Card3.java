package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.server.controller.logic.ActionPhase;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.common.messages.GameMessage;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public class Card3 extends CharacterEffect{
    private final GameController gameController;
    private Island island;
    boolean finishedTowers;
    boolean threeOrLessIslands;



    public Card3(GameController gameController){
        this.gameController = gameController;
        finishedTowers = false;
        threeOrLessIslands = false;
    }



    /** calculates the influence on the chosen tower and, if some tower have to be added or removed from the island,
     * the method move correctly the towers from the players' schoolboard to the island and viceversa. If there should
     * be a union of islands, it correctly unify the islands.
     * At the end correctly sets the values of finishedTowers and threeOrLessIslands of ActionResult in ActionPhase.
     */
    public void doEffect(){

        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        Message receivedMessage;
        GameMessage gameMessage;
        do{
            receivedMessage = messageHandler.getPlayerManager(currPlayer).getLastMessage();
        }while(receivedMessage.getMessageType()!=TypeOfMessage.IslandChoice);
        gameMessage =(GameMessage)receivedMessage;
        int islandIndex = gameMessage.getValue();
        //int islandIndex = messageHandler.getValueCLI("choose the island you want to use the effect on: ",gameController.getCurrentPlayer());
        island = gameController.getGame().getIslandOfIndex(islandIndex);
        ActionPhase actionPhase = gameController.getActionPhase();
        Player moreInfluentPlayer = actionPhase.calcultateInfluence(island);


        if (moreInfluentPlayer == null){
            playerManager.stringMessageToClient("MOREINFLUENTPLAYER: none");
            //System.out.println("MOREINFLUENTPLAYER: none");
        } else {
            messageHandler.stringMessageToAllClients("MOREINFLUENTPLAYER: "+ moreInfluentPlayer.toString());
            //System.out.println("MOREINFLUENTPLAYER: "+ moreInfluentPlayer.toString());
        }

        if (moreInfluentPlayer != null){
            //isEnded is true if one player has finished his towers
            finishedTowers = actionPhase.placeTowerOfPlayer(moreInfluentPlayer, island);
            gameController.update();
            if (finishedTowers) {
                actionPhase.getActionResult().setFinishedTowers(true);
                messageHandler.stringMessageToAllClients(gameController.getCurrentPlayer().toString() + " has finished his/her Towers");

            }
            //System.out.println(gameController.getCurrentPlayer().toString() + " has finished his/her Towers");
                return;
            }

            boolean union = actionPhase.verifyUnion();

            gameController.update();

            int numIslands= this.gameController.getGame().getIslands().size();

            if(numIslands<4){
                actionPhase.getActionResult().setThreeOrLessIslands(true);

                messageHandler.stringMessageToAllClients("There are 3 or less islands");

                //System.out.println("There are 3 or less islands");
                return;
            }
        }
    }


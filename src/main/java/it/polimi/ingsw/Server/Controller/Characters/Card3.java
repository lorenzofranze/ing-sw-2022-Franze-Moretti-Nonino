package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.ActionPhase;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.ClientMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ErrorGameMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.GameMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Controller.Network.PlayerManager;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.Player;

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

        ErrorGameMessage errorGameMessage;
        GameMessage gameMessage;
        gameMessage = playerManager.readMessage(TypeOfMessage.IslandChoice);
        int islandIndex = gameMessage.getValue();
        if(islandIndex<0 || islandIndex>gameController.getGame().getIslands().size()-1){
            errorGameMessage=new ErrorGameMessage("not valid card, rechoose.");
            playerManager.sendMessage(errorGameMessage);
        }
        //int islandIndex = messageHandler.getValueCLI("choose the island you want to use the effect on: ",gameController.getCurrentPlayer());
        island = gameController.getGame().getIslandOfIndex(islandIndex);
        ActionPhase actionPhase = gameController.getActionPhase();
        Player moreInfluentPlayer = actionPhase.calcultateInfluence(island);


        if (moreInfluentPlayer == null){
            playerManager.stringMessageToClient("MOREINFLUENTPLAYER: none");

        } else {
            messageHandler.stringMessageToAllClients("MOREINFLUENTPLAYER: "+ moreInfluentPlayer.toString());

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


package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.ActionPhase;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.IntMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.Player;

public class Card3 extends CharacterEffect{
    private final GameController gameController;
    private final MessageHandler messageHandler;
    private Island island;
    boolean finishedTowers;
    boolean threeOrLessIslands;



    public Card3(GameController gameController){
        this.gameController = gameController;
        messageHandler = gameController.getMessageHandler();
        finishedTowers = false;
        threeOrLessIslands = false;
    }



    /** calculates the influence on the chosen tower and, if some tower have to be added or removed from the island,
     * the method move correctly the towers from the players' schoolboard to the island and viceversa. If there should
     * be a union of islands, it correctly unify the islands.
     * At the end correctly sets the values of finishedTowers and threeOrLessIslands of ActionResult in ActionPhase.
     */
    public void doEffect(){
        ServerMessage messageToSend= new ServerMessage(gameController.getCurrentPlayer().getNickname(), TypeOfMessage.IslandChoice);
        IntMessage receivedMessage = (IntMessage) messageHandler.communicationWithClient(gameController, messageToSend);
        int islandIndex =receivedMessage.getValue();
        //int islandIndex = messageHandler.getValueCLI("choose the island you want to use the effect on: ",gameController.getCurrentPlayer());
        island = gameController.getGame().getIslandOfIndex(islandIndex);
        ActionPhase actionPhase = gameController.getActionPhase();
        Player moreInfluentPlayer = actionPhase.calcultateInfluence(island);


        if (moreInfluentPlayer == null){
            messageHandler.stringMessageToClient(gameController,"MOREINFLUENTPLAYER: none",gameController.getCurrentPlayer().getNickname());
            //System.out.println("MOREINFLUENTPLAYER: none");
        } else {
            messageHandler.stringMessageToClient(gameController,"MOREINFLUENTPLAYER: "+ moreInfluentPlayer.toString(),gameController.getCurrentPlayer().getNickname());
            //System.out.println("MOREINFLUENTPLAYER: "+ moreInfluentPlayer.toString());
        }

        if (moreInfluentPlayer != null){
            //isEnded is true if one player has finished his towers
            finishedTowers = actionPhase.placeTowerOfPlayer(moreInfluentPlayer, island);
            gameController.update();
            if (finishedTowers) {
                actionPhase.getActionResult().setFinishedTowers(true);
                for(Player player: gameController.getGame().getPlayers())
                {
                    messageHandler.stringMessageToClient(gameController,gameController.getCurrentPlayer().toString() + " has finished his/her Towers",player.getNickname());
                }
            }
            //System.out.println(gameController.getCurrentPlayer().toString() + " has finished his/her Towers");
                return;
            }

            boolean union = actionPhase.verifyUnion();

            gameController.update();

            int numIslands= this.gameController.getGame().getIslands().size();

            if(numIslands<4){
                actionPhase.getActionResult().setThreeOrLessIslands(true);
                for(Player player: gameController.getGame().getPlayers())
                {
                    messageHandler.stringMessageToClient(gameController,"There are 3 or less islands",player.getNickname());
                }
                //System.out.println("There are 3 or less islands");
                return;
            }
        }
    }


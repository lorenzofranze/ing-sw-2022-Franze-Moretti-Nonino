package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.ActionPhase;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.CharacterStateStudent;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public class Card3Effect extends CharacterEffect{
    protected Island island;
    protected Boolean finishedTowers;
    protected Boolean threeOrLessIslands;

    public Card3Effect(GameController gameController, CharacterState characterState){
        super(gameController, characterState );
        island = new Island();
        finishedTowers = false;
        threeOrLessIslands = false;
    }

    /**
     * Calculates the influence on the chosen island and, if some tower have to be added or removed from the island,
     * the method move correctly the towers from the players' schoolboard to the island and viceversa. If there should
     * be a union of islands, it correctly unify the islands.
     * At the end correctly sets the values of finishedTowers and threeOrLessIslands of ActionResult in ActionPhase.
     */
    public void doEffect(){

        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        ErrorMessage errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;

        int islandIndex = -1;

        boolean valid;
        do{
            valid = true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.IslandChoice);

            gameMessage = (GameMessage) receivedMessage;
            islandIndex = gameMessage.getValue();

            if(islandIndex<0 || islandIndex>gameController.getGame().getIslands().size()-1){
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorGameMessage);
                valid = false;
            }
        }while(!valid);

        island = gameController.getGame().getIslandOfIndex(islandIndex);
        ActionPhase actionPhase = gameController.getActionPhase();
        Player moreInfluentPlayer = actionPhase.calcultateInfluence(island);

        AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
        playerManager.sendMessage(ackMessage);

        if (moreInfluentPlayer != null){
            //isEnded is true if one player has finished his towers
            finishedTowers = actionPhase.placeTowerOfPlayer(moreInfluentPlayer, island);
            if (finishedTowers) {
                actionPhase.getActionResult().setFinishedTowers(true);
                return;
            }

            boolean union = actionPhase.verifyUnion();
            int numIslands= this.gameController.getGame().getIslands().size();
            if(numIslands<4){
                actionPhase.getActionResult().setThreeOrLessIslands(true);
            }
        }
    }

    @Override
    public Player effectInfluence(Island island) {
        return null;
    }
}


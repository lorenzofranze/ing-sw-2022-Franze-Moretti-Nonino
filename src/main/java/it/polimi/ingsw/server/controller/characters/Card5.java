package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.Island;

import java.util.ArrayList;
import java.util.List;

public class Card5 extends CharacterEffect{

    private final GameController gameController;
    private int NoEntryTilesLeft;
    private List<Island> blockedIslands;
    MessageHandler messageHandler;

    /**In Setup, put the 4 No Entry tiles on this card.
     * Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there,
     * put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place ant Towers.*/
    public Card5(GameController gameController){
        this.gameController = gameController;
        messageHandler = gameController.getMessageHandler();
        NoEntryTilesLeft = 4;
        blockedIslands = new ArrayList<>();
        for(Island is : gameController.getGame().getIslands()){
            is.setNumNoEntryTile(0);
        }
    }

    public void doEffect(){
        int chosenIslandIndex;

        if (NoEntryTilesLeft == 0) {
            System.out.println("There are no No Entry tiles available");
            return;
        }

        boolean valid = true;
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        ErrorMessage errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;

        do{
            valid = true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.IslandChoice);
            if(receivedMessage == null){
                System.out.println("ERROR-Card5-1");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            chosenIslandIndex = gameMessage.getValue();
            Island chosenIsland = gameController.getGame().getIslandOfIndex(chosenIslandIndex);
            if(chosenIsland == null){
                //System.out.println("The island chosen doesn't exist");
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorGameMessage);
                valid = false;
            }
            if (valid == true){
                NoEntryTilesLeft--;
                chosenIsland.setNumNoEntryTile(chosenIsland.getNumNoEntryTile()+1);
                blockedIslands.add(chosenIsland);
            }
        }while(valid == false);

        return;
    }

    public void addNoEntryTile(){
        this.NoEntryTilesLeft++;
    }

}


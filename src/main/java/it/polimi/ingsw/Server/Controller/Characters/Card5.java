package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.ClientMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ErrorGameMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.GameMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Controller.Network.PlayerManager;
import it.polimi.ingsw.Server.Model.Island;

import java.util.ArrayList;
import java.util.List;

public class Card5 extends CharacterEffectInitialize{

    private final GameController gameController;
    private int NoEntryTilesLeft;
    private List<Island> blockedIslands;
    MessageHandler messageHandler;

    public void initializeCard() {
        NoEntryTilesLeft = 4;
        blockedIslands = new ArrayList<>();
        for(Island is : gameController.getGame().getIslands()){
            is.setNumNoEntryTile(0);
        }
    }


    /**In Setup, put the 4 No Entry tiles on this card.
     * Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there,
     * put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place ant Towers.*/
    public Card5(GameController gameController){
        this.gameController = gameController;
        messageHandler = gameController.getMessageHandler();
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
        ErrorGameMessage errorGameMessage;
        GameMessage gameMessage;

        do{
            valid = true;
            gameMessage = playerManager.readMessage(TypeOfMessage.IslandChoice);
            chosenIslandIndex = gameMessage.getValue();
            Island chosenIsland = gameController.getGame().getIslandOfIndex(chosenIslandIndex);
            if(chosenIsland == null){
                errorGameMessage=new ErrorGameMessage("the island doesn't exists.");
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


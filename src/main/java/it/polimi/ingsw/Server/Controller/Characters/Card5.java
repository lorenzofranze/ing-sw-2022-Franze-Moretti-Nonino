package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.IntMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Model.Island;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

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

        do{
            valid = true;
            ServerMessage messageToSend= new ServerMessage(gameController.getCurrentPlayer().getNickname(), TypeOfMessage.IslandChoice);
            IntMessage receivedMessage = (IntMessage) messageHandler.communicationWithClient(gameController, messageToSend);
            chosenIslandIndex = receivedMessage.getValue();
            Island chosenIsland = gameController.getGame().getIslandOfIndex(chosenIslandIndex);
            if(chosenIsland == null){
                messageHandler.stringMessageToClient(gameController,"The island chosen doesn't exist", gameController.getCurrentPlayer().getNickname());
                //System.out.println("The island chosen doesn't exist");
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


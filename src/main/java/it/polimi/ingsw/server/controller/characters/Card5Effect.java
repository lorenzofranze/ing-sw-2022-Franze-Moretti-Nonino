package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.CharacterStateNoEntryTile;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

import java.util.ArrayList;
import java.util.List;

public class Card5Effect extends CharacterEffect{

    /**In Setup, put the 4 No Entry tiles on this card.
     * Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there,
     * put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place ant Towers.*/
    public Card5Effect(GameController gameController, CharacterState characterState){
        super(gameController, characterState);
        for(int i=0; i<4; i++)
            ((CharacterStateNoEntryTile)(this.characterState)).addNoEntryTile();

    }

    public void doEffect(){
        MessageHandler messageHandler = gameController.getMessageHandler();
        int chosenIslandIndex;
        boolean valid = true;
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        ErrorMessage errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;
        Island chosenIsland;

        do{
            valid = true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.IslandChoice);
            if(receivedMessage == null){
                System.out.println("ERROR-Card5-1");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            chosenIslandIndex = gameMessage.getValue();
            chosenIsland = gameController.getGame().getIslandOfIndex(chosenIslandIndex);
            if(chosenIsland == null){
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorGameMessage);
                valid = false;
            }
        }while(valid == false);
        if(((CharacterStateNoEntryTile)characterState).getNumNoEntry()>=1) {
            ((CharacterStateNoEntryTile) characterState).removeNoEntryTile();
            chosenIsland.setNumNoEntryTile(chosenIsland.getNumNoEntryTile() + 1);
        }
        //so if there is no entry tile the player has lost his money OK: he wasn't focused

        return;
    }

    @Override
    public Player effectInfluence(Island island) {
        return null;
    }

    public void addNoEntryTile(){
        ((CharacterStateNoEntryTile)characterState).addNoEntryTile();
    }

}


package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.Message;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.common.messages.GameMessage;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public class Card9 extends CharacterEffectInfluence{
    private GameController gameController;
    private ColourPawn colourPawn;

    public Card9(GameController gameController){
        this.gameController = gameController;
    }

    @Override
    public void doEffect() {
        boolean valid;
        int index;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        Message errorGameMessage;
        GameMessage gameMessage;
        do{
            valid = true;
            gameMessage = playerManager.readMessage(TypeOfMessage.StudentColour);
            index= gameMessage.getValue();
            if(index<0 || index >4){
                valid = false;
                //the island doesn't exist
                errorGameMessage=new Message(TypeOfMessage.Error);
                playerManager.sendMessage(errorGameMessage);
            }
        }while(!valid);

        this.colourPawn = ColourPawn.get(index);

    }

    public Player effectInfluence(Island island) {
        Player moreInfluent=null;
        int maxInfluence=0;
        int currScore;
        for(Player p: gameController.getGame().getPlayers()) {
            currScore = 0;
            for (ColourPawn professor : ColourPawn.values()) {
                if(professor!=this.colourPawn && p.getSchoolBoard().getProfessors().get(professor)==1)
                    currScore += island.getStudents().get(professor);
            }
            if (p.getColourTower() == island.getTowerColour()) {
                currScore += island.getTowerCount();
            }

            if (currScore > 0 && currScore > maxInfluence) {
                maxInfluence = currScore;
                moreInfluent = p;
            }
        }

        return moreInfluent;

    }
}

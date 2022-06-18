package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public class Card9Effect extends CharacterEffect{
    protected ColourPawn colourPawn;

    public Card9Effect(GameController gameController, CharacterState characterState) {
        super(gameController, characterState);
    }

    @Override
    public void doEffect() {
        boolean valid;
        int index;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        ErrorMessage errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;
        do{
            valid = true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
            gameMessage = (GameMessage) receivedMessage;
            index= gameMessage.getValue();
            if(index<0 || index >4) {
                valid = false;
                errorGameMessage = new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorGameMessage);
            }
        }while(!valid);

        this.colourPawn = ColourPawn.get(index);
        AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
        playerManager.sendMessage(ackMessage);
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

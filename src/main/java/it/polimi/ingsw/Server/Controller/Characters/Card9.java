package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.ClientMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.IntMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Controller.Network.PlayerManager;
import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.Player;

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
        ClientMessage receivedMessage;
        IntMessage intMessage;
        do{
            valid = true;
            do{
                receivedMessage = messageHandler.getPlayerManager(currPlayer).getLastMessage();
            }while(receivedMessage.getMessageType()!=TypeOfMessage.StudentColour);
            intMessage=(IntMessage)receivedMessage;
            index= intMessage.getValue();
            if(index<0 || index >4)
                valid = false;
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

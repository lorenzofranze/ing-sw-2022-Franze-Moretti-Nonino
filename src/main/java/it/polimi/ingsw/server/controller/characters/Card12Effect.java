package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public class Card12Effect extends CharacterEffect{

    public Card12Effect(GameController gameController, CharacterState characterState) {
        super(gameController, characterState);
    }
    public void doEffect(){
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        ErrorMessage errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;
        boolean valid;
        ColourPawn colourPawn;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        int chosenPawn; // index of ColourPawn enumeration

        do{
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
            if(receivedMessage == null){
                System.out.println("ERROR-card1-1");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            chosenPawn = gameMessage.getValue();

            valid = false;
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn){
                    valid=true;
                }
            }

            if(!valid){
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorGameMessage);
            }

        }while(!valid);

        colourPawn=ColourPawn.values()[chosenPawn];

        for(Player p: gameController.getGame().getPlayers()){
            if(p.getSchoolBoard().getDiningRoom().get(colourPawn)>=3){
                p.getSchoolBoard().getDiningRoom().remove(colourPawn,3);
                gameController.getGame().getStudentsBag().add(colourPawn,3);
            }
            else{
                for(ColourPawn c: ColourPawn.values()){
                    int num=p.getSchoolBoard().getDiningRoom().get(c);
                    p.getSchoolBoard().getDiningRoom().remove(colourPawn, num);
                    gameController.getGame().getStudentsBag().add(colourPawn,num);
                }
            }
        }
        AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
        playerManager.sendMessage(ackMessage);
    }

    @Override
    public Player effectInfluence(Island island) {
        return null;
    }
}

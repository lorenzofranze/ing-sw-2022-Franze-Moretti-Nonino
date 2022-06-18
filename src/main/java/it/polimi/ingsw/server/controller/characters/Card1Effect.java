package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.*;

public class Card1Effect extends CharacterEffect{

    public Card1Effect(GameController gameController, CharacterState characterState){
        super(gameController, characterState);
        for(int i=0; i<4; i++){
            ((CharacterStateStudent)(this.characterState)).addStudent(gameController.getGame().getStudentsBag().removeRandomly());
        }
    }

    public void doEffect(){
        boolean valid;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        ErrorMessage errorGameMessage;
        PawnMovementMessage gameMessage;
        Message receivedMessage;
        int indexColour; // index of ColourPawn enumeration
        int where=0; // index island
        do{
            valid = true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.PawnMovement);
            gameMessage = (PawnMovementMessage) receivedMessage;
            indexColour = gameMessage.getColour();
            if(indexColour<=-1 || indexColour>=5){
                valid=false;
            }
            valid = false;
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==indexColour && ((CharacterStateStudent)characterState).getAllStudents().get(p)>=1 ){
                    valid=true;
                }
            }
            if(valid==false){
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);  // no student
                playerManager.sendMessage(errorGameMessage);
            }
            if(valid){
                where = gameMessage.getWhere();
                if( where <0 || where > gameController.getGame().getIslands().size()-1 ) {
                    valid = false;
                    errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice); // destination not valid
                    playerManager.sendMessage(errorGameMessage);
                }
            }
        }while(!valid);

        ((CharacterStateStudent)characterState).removeStudent(ColourPawn.values()[indexColour]);
        gameController.getGame().getIslands().get(where).getStudents().add(ColourPawn.values()[indexColour]);
        if(gameController.getGame().getStudentsBag().pawnsNumber()>=1){
            ((CharacterStateStudent)characterState).addStudent(gameController.getGame().getStudentsBag().removeRandomly());
        }
        AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
        playerManager.sendMessage(ackMessage);

    }

    public Player effectInfluence(Island island) {
        return null;
    }

}

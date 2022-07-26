package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.*;

public class Card11Effect extends CharacterEffect{

    /**
     * Places 4 students on this card randomly
     * @param gameController
     * @param characterState
     */
    public Card11Effect(GameController gameController, CharacterState characterState){
        super(gameController, characterState);
        for(int i=0; i<4; i++){
            ((CharacterStateStudent)(this.characterState)).addStudent(gameController.getGame().getStudentsBag().removeRandomly());
        }
    }

    /**
     * Handles the player who can take a student from the card and put it on his gameboard until he makes valid moves.
     * Remove that pawn from the card and replace it with another taken from the bag.
     */
    public void doEffect(){
        boolean valid;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        ErrorMessage errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;
        int chosenPawn; // index of ColourPawn enumeration

        do{
            valid=true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
            gameMessage = (GameMessage) receivedMessage;
            chosenPawn = gameMessage.getValue();

            if(chosenPawn <=-1 || chosenPawn >=5){
                valid=false;
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice); // index colour invalid
                playerManager.sendMessage(errorGameMessage);
            }
            valid = false;
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn  && ((CharacterStateStudent)characterState).getAllStudents().get(p)>=1 ){
                    valid=true;
                }
            }
            if(valid==false){
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorGameMessage);
            }

        }while(!valid);

        ((CharacterStateStudent)characterState).removeStudent(ColourPawn.values()[chosenPawn]);

        PawnsMap chosenPawnsMap= new PawnsMap();
        chosenPawnsMap.add(ColourPawn.values()[chosenPawn]);

        gameController.getCurrentPlayer().getSchoolBoard().addToDiningRoom(chosenPawnsMap, gameController.getGame());


        if(gameController.getGame().getStudentsBag().pawnsNumber()>=1){
            ((CharacterStateStudent)characterState).addStudent(gameController.getGame().getStudentsBag().removeRandomly());
        }
        AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
        playerManager.sendMessage(ackMessage);

    }

    @Override
    public Player effectInfluence(Island island) {
        return null;
    }

}

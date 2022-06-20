package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.CharacterState;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;

public class Card10Effect extends CharacterEffect{

    public Card10Effect(GameController gameController, CharacterState characterState) {
        super(gameController, characterState);
    }

    /**
     * Handles the choices of the players (util they are valid choices) about:
     * -> how many students to move (maximum 2)
     * -> the student in the player entrance (also there can be the error of full-diningroom)
     * -> the student in his dining room
     * and swaps the pawns
     */
    public void doEffect() {
        boolean valid;
        int count;
        int i, colourEntrance, colourDining;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        Message errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;

        // player chooses how many students to move
        do {
            valid=true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.NumOfMove);
            gameMessage = (GameMessage) receivedMessage;
            count = gameMessage.getValue();
            if (count<0 || count > 2 || gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().pawnsNumber()<count) {  // max 3 movements
                valid = false;
                errorGameMessage = new ErrorMessage(TypeOfError.InvalidChoice); // index colour invalid
                playerManager.sendMessage(errorGameMessage);
            }
        }while(!valid);
        AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
        playerManager.sendMessage(ackMessage);

        for (i=0; i<count; i++){
            //player chooses student in his entrance
            do{
                valid = true;
                receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
                gameMessage = (GameMessage) receivedMessage;
                colourEntrance = gameMessage.getValue();
                if(colourEntrance<=-1 || colourEntrance >=5){
                    valid=false;
                    // to user: index not valid
                    errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                    playerManager.sendMessage(errorGameMessage);
                }
                if(valid){
                    if (gameController.getCurrentPlayer().getSchoolBoard()
                            .getEntrance().get(ColourPawn.get(colourEntrance)) <= 0){
                        valid = false;
                        //to user: change color pawn to move, you don't have that color
                        errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                        playerManager.sendMessage(errorGameMessage);
                    }
                }
                if(valid && gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().
                        get(ColourPawn.get(colourEntrance))>=10) {
                    valid = false;
                    errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice, "dining table full");
                    playerManager.sendMessage(errorGameMessage);
                }
            }while(!valid);
            ackMessage = new AckMessage(TypeOfAck.CorrectMove);
            playerManager.sendMessage(ackMessage);


            // now chooses the student in his dining room
            do{
                valid=true;
                receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
                gameMessage = (GameMessage) receivedMessage;
                colourDining= gameMessage.getValue();
                if(colourDining<=-1 || colourDining >=5){
                    valid=false;
                    // to user: index not valid
                    errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                    playerManager.sendMessage(errorGameMessage);
                }
                if(valid && gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().get(ColourPawn.get(colourDining)) <=0){
                    valid = false;
                    errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                    playerManager.sendMessage(errorGameMessage);
                }
            }while(!valid);
            ackMessage = new AckMessage(TypeOfAck.CorrectMove);
            playerManager.sendMessage(ackMessage);

            gameController.getCurrentPlayer().getSchoolBoard().swap(ColourPawn.get(colourEntrance), ColourPawn.get(colourDining), gameController.getGame());
            gameController.update();

            ackMessage = new AckMessage(TypeOfAck.CorrectMove);
            playerManager.sendMessage(ackMessage);
        }
    }

    @Override
    public Player effectInfluence(Island island) {
        return null;
    }
}

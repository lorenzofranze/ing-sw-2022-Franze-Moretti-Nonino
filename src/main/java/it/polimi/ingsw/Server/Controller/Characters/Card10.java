package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.ClientMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.IntMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Controller.Network.PlayerManager;
import it.polimi.ingsw.Server.Model.ColourPawn;

public class Card10 extends CharacterEffect{
    private GameController gameController;

    public Card10(GameController gameController){
        this.gameController=gameController;
    }

    public void doEffect() {
        boolean valid;
        int num=0;
        int i, colourEntrance, colourDining;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        ClientMessage receivedMessage;
        IntMessage intMessage;

        do{
            valid=true;
            do{
                receivedMessage = messageHandler.getPlayerManager(currPlayer).getLastMessage();
            }while(receivedMessage.getMessageType()!=TypeOfMessage.Number);
            intMessage=(IntMessage)receivedMessage;
            num = intMessage.getValue();
            if(num<0 || num >2)
                valid = false;
        }while(!valid);

        for (i=0; i<num; i++){
            do{
                valid = true;
                // to user: choose one color pawn

                do{
                    receivedMessage = messageHandler.getPlayerManager(currPlayer).getLastMessage();
                }while(receivedMessage.getMessageType()!=TypeOfMessage.StudentColour);
                intMessage=(IntMessage)receivedMessage;
                colourEntrance = intMessage.getValue();
                if(colourEntrance<=-1 || colourEntrance >=5){
                    valid=false;
                    // to user: index not valid
                    playerManager.stringMessageToClient("indexColour not valid.");
                    //System.out.println("indexColour not valid.");
                }
                if(valid){
                    if (gameController.getCurrentPlayer().getSchoolBoard()
                            .getEntrance().get(ColourPawn.get(colourEntrance)) <= 0){
                        valid = false;
                        //to user: change color pawn to move, you don't have that color
                        playerManager.stringMessageToClient("You don't have that colour.");
                        //System.out.println("You don't have that colour.");
                    }
                }
                if(valid && gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().
                        get(ColourPawn.get(colourEntrance))>=10) {
                    valid = false;
                    // to user: your school board in that row of your dining room is full
                }

            }while(!valid);

            do{
                valid=true;
                do{
                    receivedMessage = messageHandler.getPlayerManager(currPlayer).getLastMessage();
                }while(receivedMessage.getMessageType()!=TypeOfMessage.StudentColour);
                intMessage=(IntMessage)receivedMessage;
                colourDining= intMessage.getValue();
                if(gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().get(ColourPawn.get(colourDining)) <=0)
                    valid = false;
            }while(!valid);

            gameController.getCurrentPlayer().getSchoolBoard().swap(ColourPawn.get(colourEntrance), ColourPawn.get(colourDining), gameController.getGame());
        }
    }
}

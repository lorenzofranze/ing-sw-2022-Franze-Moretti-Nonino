package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.IntMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
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
        do{
            valid=true;
            ServerMessage messageToSend= new ServerMessage(gameController.getCurrentPlayer().getNickname(), TypeOfMessage.Number);
            IntMessage receivedMessage = (IntMessage) messageHandler.communicationWithClient(gameController, messageToSend);
            num = receivedMessage.getValue();
            if(num<0 || num >2)
                valid = false;
        }while(!valid);

        for (i=0; i<num; i++){
            do{
                valid = true;
                // to user: choose one color pawn

                ServerMessage messageToSend= new ServerMessage(gameController.getCurrentPlayer().getNickname(), TypeOfMessage.StudentColour);
                IntMessage receivedMessage = (IntMessage) messageHandler.communicationWithClient(gameController, messageToSend);
                colourEntrance = receivedMessage.getValue();
                if(colourEntrance<=-1 || colourEntrance >=5){
                    valid=false;
                    // to user: index not valid
                    System.out.println("indexColour not valid.");
                }
                if(valid){
                    if (gameController.getCurrentPlayer().getSchoolBoard()
                            .getEntrance().get(ColourPawn.get(colourEntrance)) <= 0){
                        valid = false;
                        //to user: change color pawn to move, you don't have that color
                        System.out.println("You don't have that colour.");
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
                ServerMessage messageToSend= new ServerMessage(gameController.getCurrentPlayer().getNickname(), TypeOfMessage.StudentColour);
                IntMessage receivedMessage = (IntMessage) messageHandler.communicationWithClient(gameController, messageToSend);
                colourDining= receivedMessage.getValue();
                if(gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().get(ColourPawn.get(colourDining)) <=0)
                    valid = false;
            }while(!valid);

            gameController.getCurrentPlayer().getSchoolBoard().swap(ColourPawn.get(colourEntrance), ColourPawn.get(colourDining), gameController.getGame());
        }
    }
}

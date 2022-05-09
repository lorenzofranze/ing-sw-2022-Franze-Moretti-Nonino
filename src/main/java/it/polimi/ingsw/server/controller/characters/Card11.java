package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.PawnsMap;

public class Card11 extends CharacterEffect{
    private final GameController gameController;
    private PawnsMap pawns;

    public Card11(GameController gameController){
        this.gameController = gameController;
        pawns = new PawnsMap();
        pawns.add(gameController.getGame().getStudentsBag().removeRandomly(4));
    }

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
            valid=false;
            //chosenPawn = messageHandler.getValueCLI("choose one color pawn: ",gameController.getCurrentPlayer());
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
            if(receivedMessage==null){
                System.out.println("ERROR-Card11-1");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            chosenPawn = gameMessage.getValue();

            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn && pawns.get(p)>=1 ){
                    valid=true;
                }
            }
            if(!valid){
                //the island doesn't exists
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorGameMessage);
            }

        }while(!valid);

        pawns.remove(ColourPawn.values()[chosenPawn]);

        PawnsMap chosenPawnsMap= new PawnsMap();
        chosenPawnsMap.add(ColourPawn.values()[chosenPawn], 1);

        gameController.getCurrentPlayer().getSchoolBoard().addToDiningRoom(chosenPawnsMap, gameController.getGame());


        if(gameController.getGame().getStudentsBag().pawnsNumber()>=1){
            pawns.add(gameController.getGame().getStudentsBag().removeRandomly());
        }

    }



    public void showPawns(){
        //mostra pawns sulla carta
    }
}

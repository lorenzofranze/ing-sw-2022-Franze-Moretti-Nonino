package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.PawnsMap;

public class Card1 extends CharacterEffect{

    private final GameController gameController;
    private PawnsMap pawns;

    public Card1(GameController gameController){
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
        int chosenIsland; // index island
        do{
            valid=false;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
            if(receivedMessage == null){
                System.out.println("ERROR-card1-1");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            chosenPawn = gameMessage.getValue();
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn && pawns.get(p)>=1 ){
                    valid=true;
                }
            }

        }while(!valid);
        pawns.remove(ColourPawn.values()[chosenPawn]);

        do {
            valid = true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.IslandChoice);
            if(receivedMessage == null){
                System.out.println("ERROR-card1-2");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            chosenIsland= gameMessage.getValue();
            if(chosenIsland<0 || chosenIsland>gameController.getGame().getIslands().size()-1){
                valid=false;
                //not valid card, rechoose
                errorGameMessage=new ErrorMessage(TypeOfError.InvalidChoice);
                playerManager.sendMessage(errorGameMessage);
            }
        }while(!valid);

        gameController.getGame().getIslands().get(chosenIsland).getStudents().add(ColourPawn.values()[chosenPawn]);

        if(gameController.getGame().getStudentsBag().pawnsNumber()>=1){
            pawns.add(gameController.getGame().getStudentsBag().removeRandomly());
        }

    }

}

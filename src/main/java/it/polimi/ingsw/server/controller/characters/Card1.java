package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.common.messages.GameMessage;
import it.polimi.ingsw.common.messages.TypeOfMessage;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.PawnsMap;

public class Card1 extends CharacterEffectInitialize{

    private final GameController gameController;
    private PawnsMap pawns;

    public Card1(GameController gameController){
        this.gameController = gameController;
        pawns = new PawnsMap();
    }

    public void initializeCard() {
        pawns.add(gameController.getGame().getStudentsBag().removeRandomly(4));
    }

    public void doEffect(){
        boolean valid;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        ClientMessage receivedMessage;
        GameMessage gameMessage;
        int chosenPawn; // index of ColourPawn enumeration
        int chosenIsland; // index island
        do{
            valid=false;
            do{
                receivedMessage = messageHandler.getPlayerManager(currPlayer).getLastMessage();
            }while(receivedMessage.getMessageType()!=TypeOfMessage.StudentColour);
            gameMessage =(GameMessage)receivedMessage;
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
            do{
                receivedMessage = messageHandler.getPlayerManager(currPlayer).getLastMessage();
            }while(receivedMessage.getMessageType()!=TypeOfMessage.IslandChoice);
            gameMessage =(GameMessage)receivedMessage;
            chosenIsland= gameMessage.getValue();
            if(chosenIsland<0 || chosenIsland>gameController.getGame().getIslands().size()-1){
                valid=false;
            }
        }while(!valid);

        gameController.getGame().getIslands().get(chosenIsland).getStudents().add(ColourPawn.values()[chosenPawn]);

        if(gameController.getGame().getStudentsBag().pawnsNumber()>=1){
            pawns.add(gameController.getGame().getStudentsBag().removeRandomly());
        }

    }

    public void showPawns(){
        //mostra pawns sulla carta

    }
}

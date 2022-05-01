package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.ClientMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ErrorGameMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.GameMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Controller.Network.PlayerManager;
import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.PawnsMap;

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
        ErrorGameMessage errorGameMessage;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);

        GameMessage gameMessage;
        int chosenPawn; // index of ColourPawn enumeration
        int chosenIsland; // index island
        do{
            valid=false;
            gameMessage = playerManager.readMessage(TypeOfMessage.StudentColour);
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
            gameMessage = playerManager.readMessage(TypeOfMessage.IslandChoice);
            chosenIsland= gameMessage.getValue();
            if(chosenIsland<0 || chosenIsland>gameController.getGame().getIslands().size()-1){
                valid=false;
                errorGameMessage=new ErrorGameMessage("not valid card, rechoose.");
                playerManager.sendMessage(errorGameMessage);
            }
        }while(!valid);

        gameController.getGame().getIslands().get(chosenIsland).getStudents().add(ColourPawn.values()[chosenPawn]);

        if(gameController.getGame().getStudentsBag().pawnsNumber()>=1){
            pawns.add(gameController.getGame().getStudentsBag().removeRandomly());
        }

    }

}

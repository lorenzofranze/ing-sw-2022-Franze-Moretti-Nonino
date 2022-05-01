package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.ClientMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.IntMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Controller.Network.PlayerManager;
import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.PawnsMap;
import it.polimi.ingsw.Server.Model.Player;

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
        IntMessage intMessage;
        int chosenPawn; // index of ColourPawn enumeration
        int chosenIsland; // index island
        do{
            valid=false;
            do{
                receivedMessage = messageHandler.getPlayerManager(currPlayer).getLastMessage();
            }while(receivedMessage.getMessageType()!=TypeOfMessage.StudentColour);
            intMessage=(IntMessage)receivedMessage;
            chosenPawn = intMessage.getValue();
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
            intMessage=(IntMessage)receivedMessage;
            chosenIsland= intMessage.getValue();
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

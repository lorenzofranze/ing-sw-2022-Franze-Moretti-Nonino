package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.PawnsMap;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public class Card11 extends CharacterEffectInitialize{
    private final GameController gameController;
    private PawnsMap pawns;

    public Card11(GameController gameController){
        this.gameController = gameController;
        pawns = new PawnsMap();
    }


    public void initializeCard() {
        pawns.add(gameController.getGame().getStudentsBag().removeRandomly(4));
    }

    public void doEffect(){
        boolean valid;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int chosenPawn; // index of ColourPawn enumeration

        do{
            valid=false;
            chosenPawn = messageHandler.getValueCLI("choose one color pawn: ",gameController.getCurrentPlayer());
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn && pawns.get(p)>=1 ){
                    valid=true;
                }
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

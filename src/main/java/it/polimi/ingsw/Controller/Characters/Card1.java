package it.polimi.ingsw.Controller.Characters;

import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Controller.MessageHandler;
import it.polimi.ingsw.Model.ColourPawn;
import it.polimi.ingsw.Model.PawnsMap;

public class Card1 implements CharacterEffect{

    private final GameController gameController;
    private PawnsMap pawns;

    public Card1(GameController gameController){
        this.gameController = gameController;
        pawns = new PawnsMap();
    }

    public void initializeCard() {
        pawns.add(gameController.getGame().getStudentsBag().removeRandomly(4));
    }

    public void show(){
        //mostra pawns sulla carta

    }

    public void doEffect(){
        boolean valid;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int chosenPawn; // index of ColourPawn enumeration
        int chosenIsland; // index island
        do{
            valid=false;
            chosenPawn = messageHandler.getValueCLI("choose one color pawn: ",gameController.getCurrentPlayer());
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn && pawns.get(p)>=1 ){
                    valid=true;
                }
            }

        }while(valid);
        pawns.remove(ColourPawn.values()[chosenPawn]);

        do {
            valid = true;
            chosenIsland = messageHandler.getValueCLI("choose one island: ", gameController.getCurrentPlayer());
            if(chosenIsland<0 || chosenIsland>gameController.getGame().getIslands().size()-1){
                valid=false;
            }
        }while(!valid);

        gameController.getGame().getIslands().get(chosenIsland).getStudents().add(ColourPawn.values()[chosenPawn]);

    }
}

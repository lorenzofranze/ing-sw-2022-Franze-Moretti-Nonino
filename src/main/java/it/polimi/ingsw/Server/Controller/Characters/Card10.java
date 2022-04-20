package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;

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
            num = messageHandler.getValueCLI("quante pedine vuoi cambiare?(max 2)", gameController.getCurrentPlayer());
            if(num<0 || num >2)
                valid = false;
        }while(!valid);

        for (i=0; i<num; i++){
            do{
                valid = true;
                colourEntrance = messageHandler.getValueCLI("colore studente all'ingresso", gameController.getCurrentPlayer());
                if(colourEntrance<0 || colourEntrance>4){

                }

            }while(!valid);

        }

    }

    boolean valid;
    MessageHandler messageHandler = this.gameController.getMessageHandler();
}

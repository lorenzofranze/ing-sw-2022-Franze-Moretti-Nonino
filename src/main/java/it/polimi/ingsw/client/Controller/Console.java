package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.server.controller.logic.GameMode;

public class Console {

    ClientController clientController = ClientController.getInstance();
    GameMode gameMode = clientController.getGameMode();

    private enum ActionBookMark{}

    Phase currentPhase;
    ActionBookMark currActionBookMark;



    public void play(){
        currentPhase = clientController.getGameStatePojo().getCurrentPhase();

        switch (currentPhase){
            case PIANIFICATION:
                playPianification();
                break;
            case ACTION:
                playAction();
                break;
        }
    }

    private void playPianification(){

    }

    private void playAction(){
        switch (currActionBookMark){

        }

    }
}

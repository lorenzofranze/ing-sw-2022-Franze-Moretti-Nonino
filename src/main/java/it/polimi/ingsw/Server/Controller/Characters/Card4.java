package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.Player;

public class Card4 {
    private final GameController gameController;
    private Player player; //NB

    public Card4(GameController gameController, Player player){
        this.gameController = gameController;
        this.player=player;
    }

    public void initializeCard() {
        //empty
    }

    /**
     * During the actionPhase the list is checked: if the current player is in the list, he can choose to move
     * mother island to steps more than normally permitted.
     * Then in the "move mother nature method" (in the action phase) the player is removed from the list
     * after his choise is taken.
     */
    public void doEffect(){
        gameController.addPlayersCard4(player);
    }

}

//DECORATOR???????????? it modifies move mother nature method in action phase
package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Character;

import java.util.List;

public class GameStateMessage extends ServerMessage {

    private Game game;

    public GameStateMessage(String nickname, TypeOfMessage typeOfMessage, GameController gameController) {
        super(nickname, typeOfMessage);
        this.game = gameController.getGame();
    }
}

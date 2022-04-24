package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Controller.Characters.CharacterEffect;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Character;

import java.util.Map;

public class GameStateMessage extends ServerMessage {

    private Game game;
    private String currentPlayer;
    private Map<Character, CharacterEffect> characterEffects;
    private String currentPhase;

    public GameStateMessage(String nickname, TypeOfMessage typeOfMessage, GameController gameController) {
        super(nickname, typeOfMessage);
        this.game = gameController.getGame();
        this.currentPlayer=gameController.getCurrentPlayer().getNickname();
        this.characterEffects= gameController.getCharacterEffects();
        this.currentPhase= gameController.getCurrentPhase().toString();

    }
}

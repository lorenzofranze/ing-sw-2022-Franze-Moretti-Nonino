package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Character;

import java.util.List;

public class GameStateMessage extends ServerMessage {

    private Integer gameId;
    private List<Player> players;
    private Player currentPlayer;
    private List<Island> islands;
    private PawnsMap studentsBag;
    private List<Cloud> clouds;
    private PawnsMap professorsLeft;
    private it.polimi.ingsw.Server.Model.Character activeEffect;
    private int coinSupply;
    private List<Character> characters;

    public GameStateMessage(String nickname, Game game) {
        this.setMessageType(TypeOfMessage.GameState);
    }
}

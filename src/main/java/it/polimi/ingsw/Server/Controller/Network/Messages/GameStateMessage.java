package it.polimi.ingsw.Server.Controller.Network.Messages;

import it.polimi.ingsw.Server.Model.Character;
import it.polimi.ingsw.Server.Model.Cloud;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.PawnsMap;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public class GameStateMessage extends Message {

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

    public GameStateMessage(String nickname, TypeOfMessage typeOfMessage) {

    }
}

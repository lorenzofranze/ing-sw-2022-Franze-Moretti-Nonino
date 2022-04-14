package it.polimi.ingsw.server.Controller.Network.Messages;

import it.polimi.ingsw.server.Model.Character;
import it.polimi.ingsw.server.Model.Cloud;
import it.polimi.ingsw.server.Model.Island;
import it.polimi.ingsw.server.Model.PawnsMap;
import it.polimi.ingsw.server.Model.Player;

import java.util.List;

public class GameState {
    private Integer gameId;
    private List<Player> players;
    private Player currentPlayer;
    private List<Island> islands;
    private PawnsMap studentsBag;
    private List<Cloud> clouds;
    private PawnsMap professorsLeft;
    private it.polimi.ingsw.server.Model.Character activeEffect;
    private int coinSupply;
    private List<Character> characters;
}

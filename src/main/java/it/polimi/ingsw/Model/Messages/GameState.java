package it.polimi.ingsw.Model.Messages;

import it.polimi.ingsw.Model.Character;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.PawnsMap;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class GameState {
    private Integer gameId;
    private List<Player> players;
    private Player currentPlayer;
    private List<Island> islands;
    private PawnsMap studentsBag;
    private List<Cloud> clouds;
    private PawnsMap professorsLeft;
    private it.polimi.ingsw.Model.Character activeEffect;
    private int coinSupply;
    private List<Character> characters;
}

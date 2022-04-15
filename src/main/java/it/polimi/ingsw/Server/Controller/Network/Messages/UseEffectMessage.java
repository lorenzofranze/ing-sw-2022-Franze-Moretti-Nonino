package it.polimi.ingsw.Server.Controller.Network.Messages;

public class UseEffectMessage extends ClientMessage  {
    private Character effect;
    private GameState gameState;

    public GameState getGameState() {
        return gameState;
    }

    public Character getEffect() {
        return effect;
    }
}

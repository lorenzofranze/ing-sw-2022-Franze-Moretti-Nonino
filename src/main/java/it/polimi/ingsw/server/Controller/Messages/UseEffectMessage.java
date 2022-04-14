package it.polimi.ingsw.server.Controller.Messages;

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

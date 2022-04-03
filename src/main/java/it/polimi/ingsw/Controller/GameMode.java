package it.polimi.ingsw.Controller;

public enum GameMode {
    Simple_2(2), Simple_3(3), Complex_2(2), Complex_3(3);

    private final int numPlayers;

    private GameMode(int numPlayers){
        this.numPlayers=numPlayers;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }
}

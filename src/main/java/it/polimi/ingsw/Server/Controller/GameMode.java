package it.polimi.ingsw.Server.Controller;

public enum GameMode {
    Simple_2(2, false), Simple_3(3, false),
    Complex_2(2, true), Complex_3(3, true);

    private final int numPlayers;
    private final boolean expert;

    private GameMode(int numPlayers, boolean expert){
        this.numPlayers=numPlayers;
        this.expert = expert;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }
    public boolean isExpert() {return this.expert;}
}

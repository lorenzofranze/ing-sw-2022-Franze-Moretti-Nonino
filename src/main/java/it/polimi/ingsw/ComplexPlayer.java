package it.polimi.ingsw;

public class ComplexPlayer extends Player{
    private int coins;

    public ComplexPlayer(String nickName, ColourTower colourTower, ColourWizard wizard){
        super(nickName, colourTower, wizard);
        this.coins = 1;
    }

    public void addCoins(int c){
        coins = coins + c;
    }

    public void removeCoins(int c){
        coins = coins - c;
    }
}

package it.polimi.ingsw.common.gamePojo;

public enum ColourTower {
    Black(0), White(1), Grey(2);

    private final int indexColour;

    private ColourTower(int indexColour){
        this.indexColour = indexColour;
    }


    public int getIndexColour() {
        return indexColour;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

package it.polimi.ingsw.Model;

public enum ColourPawn {

    Yellow(0), Blue(1), Green(2), Red(3), Pink(4);

    private final int indexColour;

    private ColourPawn(int indexColour){
        this.indexColour = indexColour;
    }

    public int getIndexColour() {
        return indexColour;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static String toStringAll() {
        String list = new String("\nThe possible colours are:\n");
        for(ColourPawn c: ColourPawn.values()){
            list = list + c.toString() + " = " + c.getIndexColour() + "\n";
        }
        return list;
    }

    public static ColourPawn get(int indexColour){
        ColourPawn colourPawn = null;
        for(ColourPawn c: ColourPawn.values()){
            if (c.getIndexColour() == indexColour) colourPawn = c;
        }
        return colourPawn;
    }
}

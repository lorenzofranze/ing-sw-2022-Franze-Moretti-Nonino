package it.polimi.ingsw.common.gamePojo;

import java.util.*;

public class PawnsMapPojo {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PINK = "\u001B[35m";

    private static final String[] ansi_colours = {ANSI_GREEN, ANSI_RED, ANSI_YELLOW, ANSI_PINK, ANSI_BLUE, ANSI_RESET};

    private HashMap<ColourPawn, Integer> pawns;

    public HashMap<ColourPawn, Integer> getPawns() {
        return pawns;
    }

    /** @return the number of pawns */
    public int pawnsNumber(){
        int tot=0;
        ColourPawn pawnsList[] = ColourPawn.values();
        for(ColourPawn p : pawnsList) {
            tot += this.pawns.get(p);
        }
        return tot;
    }

    public void setPawns(HashMap<ColourPawn, Integer> pawns) {
        this.pawns = pawns;
    }

    /** @return a String that describes the content of the map */
    @Override
    public String toString() {
        int i=0;
        String ris = new String();
        ColourPawn pawnsList[] = ColourPawn.values();
        for (ColourPawn p : pawnsList) {
            ris = ris + ("("+ansi_colours[i]+p.toString() + " , " + this.pawns.get(p) + ansi_colours[5]+") ");
            i++;
        }
        return ris;
    }

    public PawnsMapPojo(it.polimi.ingsw.server.model.PawnsMap serverPawnsMap){
        HashMap<ColourPawn, Integer> map = new HashMap<>();
        for(ColourPawn c: ColourPawn.values()){
            map.put(c, serverPawnsMap.get(c));
        }
        this.pawns = map;
    }

    public int get(ColourPawn colour){
        return pawns.get(colour);
    }
}

package it.polimi.ingsw;

import java.util.*;

public class PawnsMap {

    private HashMap<ColourPawn, Integer> pawns;

    public PawnsMap() {
        this.pawns = new HashMap<ColourPawn, Integer>();
        ColourPawn pawnsList[] = ColourPawn.values(); // array of enum elements
        for(ColourPawn p : pawnsList){
            pawns.put(p, 0);
        }
    }


    public void removePawn(ColourPawn colour){
        pawns.put(colour, pawns.get(colour)-1);
    }

    public void addPawn(ColourPawn colour){
        pawns.put(colour, pawns.get(colour)+1);
    }

    public void addPawns(ColourPawn colour, int num){
        pawns.put(colour, pawns.get(colour)+num);
    }

    public void removePawns(ColourPawn colour, int num){
        pawns.put(colour, pawns.get(colour)-num);
    }

    /**removes one pawn from pawnsMap randomly. Returns which ColourPawn has been removed. Usefull when the pawnsMap
     * is used to implement the studentsBag.*/
    public ColourPawn removePawnRandomly(){

        List<ColourPawn> temp = new ArrayList<ColourPawn>(pawns.get(ColourPawn.Yellow));

        for (ColourPawn currColor: ColourPawn.values()){
            for (int i = 0; i < pawns.get(currColor); i++){
            temp.add(currColor);
            }
        }

        int tot = temp.size();
        Random rand = new Random();
        int randomNum = rand.nextInt(tot);
        ColourPawn toRemove = temp.get(randomNum);

        removePawn(toRemove);

        return(toRemove);
    }

    public int getPawns(ColourPawn colour){
        return pawns.get(colour);
    }

    /** @return true if there is no pawn left */
    public boolean isEmpty(){
        int tot=0;
        ColourPawn pawnsList[] = ColourPawn.values();
        for(ColourPawn p : pawnsList) {
            tot = this.pawns.get(p);
        }
        if(tot == 0)
            return true;
        return false;
    }

    /** @return the number of pawns */
    public int pawnsNumber(){
        int tot=0;
        ColourPawn pawnsList[] = ColourPawn.values();
        for(ColourPawn p : pawnsList) {
            tot = this.pawns.get(p);
        }
        return tot;
    }

    /** return a copy of the map */
    public PawnsMap clone(){
        PawnsMap tmp = new PawnsMap();
        tmp.pawns = new HashMap<ColourPawn, Integer>(this.pawns);
        return tmp;
    }

}

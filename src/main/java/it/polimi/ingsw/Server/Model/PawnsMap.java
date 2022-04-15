package it.polimi.ingsw.Server.Model;

import java.util.*;
public class PawnsMap{

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PINK = "\u001B[35m";

    private static final String[] ansi_colours = {ANSI_YELLOW, ANSI_BLUE, ANSI_GREEN, ANSI_RED, ANSI_PINK, ANSI_RESET};

    private HashMap<ColourPawn, Integer> pawns;

    public PawnsMap() {
        this.pawns = new HashMap<ColourPawn, Integer>();
        ColourPawn pawnsList[] = ColourPawn.values(); // array of enum elements
        for(ColourPawn p : pawnsList){
            pawns.put(p, 0);
        }

    }

    public void add(ColourPawn colour){
        pawns.put(colour, pawns.get(colour)+1);
    }

    public void remove(ColourPawn colour){
        pawns.put(colour, pawns.get(colour)-1);
    }

    public void add(PawnsMap toAdd){
        ColourPawn pawnsList[] = ColourPawn.values();
        for(ColourPawn p : pawnsList)
            pawns.put(p, pawns.get(p) + toAdd.get(p));
    }

    public void remove(PawnsMap toRemove) {
        ColourPawn pawnsList[] = ColourPawn.values();
        for (ColourPawn p : pawnsList) {
            pawns.put(p, pawns.get(p) - toRemove.get(p));
        }
    }

    public void add(ColourPawn colour, int num){
        pawns.put(colour, pawns.get(colour)+num);
    }

    public void remove(ColourPawn colour, int num){
        pawns.put(colour, pawns.get(colour)-num);
    }

    /**randomly removes 'num' pawns from pawnsMap, where 'num' is the int passed as a parameter.
     * @return the PawnsMap of values removed*/
    public PawnsMap removeRandomly(int num){

        List<ColourPawn> temp = new ArrayList<ColourPawn>(this.pawns.get(ColourPawn.Yellow));

        for (ColourPawn currColor: ColourPawn.values()){
            for (int i = 0; i < this.pawns.get(currColor); i++){
            temp.add(currColor);
            }
        }

        Collections.shuffle(temp);

        PawnsMap toRemove = new PawnsMap();
        for (int i = 0; i < num; i++){
            toRemove.add(temp.get(i));
        }

        this.remove(toRemove);

        return(toRemove);
    }

    /**randomly removes one pawn from pawnsMap.
     * @return which ColourPawn has been removed*/
    public ColourPawn removeRandomly(){

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

        remove(toRemove);

        return(toRemove);
    }

    public int get(ColourPawn colour){
        return pawns.get(colour);
    }

    /** @return true if there is no pawn left */
    public boolean isEmpty(){
        int tot=0;
        ColourPawn pawnsList[] = ColourPawn.values();
        for(ColourPawn p : pawnsList) {
            tot += this.pawns.get(p);
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
            tot += this.pawns.get(p);
        }
        return tot;
    }

    /** @return a copy of the PawnsMap */
    public PawnsMap clone(){
        PawnsMap tmp = new PawnsMap();
        tmp.pawns = new HashMap<ColourPawn, Integer>(this.pawns);
        return tmp;
    }

    /** compares two PawnsMap and returns a boolean */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PawnsMap that = (PawnsMap) o;
        boolean ris = true;
        ColourPawn pawnsList[] = ColourPawn.values();
        for(ColourPawn p : pawnsList) {
            if (this.pawns.get(p) != that.pawns.get(p))
                ris = false;
        }
        return ris;
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
}

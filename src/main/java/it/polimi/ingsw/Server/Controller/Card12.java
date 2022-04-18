package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.Player;

public class Card12 {
    private ColourPawn colourPawn;
    private GameController gameController;
    public Card12(GameController gameController, ColourPawn colourPawn){
        this.colourPawn=colourPawn;
    }

    public void doEffect(){
        for(Player p: gameController.getGame().getPlayers()){
            if(p.getSchoolBoard().getDiningRoom().get(colourPawn)>=3){
                p.getSchoolBoard().getDiningRoom().remove(colourPawn,3);
                gameController.getGame().getStudentsBag().add(colourPawn,3);
            }
            else{
                for(ColourPawn c: ColourPawn.values()){
                    int num=p.getSchoolBoard().getDiningRoom().get(c);
                    p.getSchoolBoard().getDiningRoom().remove(colourPawn, num);
                    gameController.getGame().getStudentsBag().add(colourPawn,num);
                }
            }
        }
    }
}

package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.PawnsMap;

public class Card7 extends CharacterEffect{

    private final GameController gameController;
    private PawnsMap pawns;

    public Card7(GameController gameController){
        this.gameController = gameController;
        pawns = new PawnsMap();
        pawns.add(gameController.getGame().getStudentsBag().removeRandomly(6));
    }

    public void doEffect(){

        ErrorMessage errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;

        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PawnsMap pawnsChosen = new PawnsMap();
        PawnsMap pawnsToRemove = new PawnsMap();
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        //System.out.println("The students on the card are:\n" + pawns);
        //System.out.println("You can choose up to 3 Students from this card and replace them with the same amount of " +
        //        "Students form your entrance ( colour pawn '-1' to end):\n");
        boolean valid;
        int chosenPawn; // index of ColourPawn enumeration
        int chosenIsland; // index island
        int count = 0;
        boolean end = false;
        do{
            valid=false;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
            if(receivedMessage == null){
                System.out.println("ERROR-Card7-1");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            chosenPawn= gameMessage.getValue();
            if (chosenPawn == -1){
                valid=true;
                end = true;
            }
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn && pawns.get(p)>=1 ){
                    valid=true;
                    count++;
                    if (count == 3){end = true;}
                    pawns.remove(ColourPawn.values()[chosenPawn]);
                    pawnsChosen.add(ColourPawn.values()[chosenPawn]);
                }
            }
        }while(!valid || (valid && end == false));

        valid = true;
        while(!valid || count > 0){
            valid=false;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
            if(receivedMessage == null){
                System.out.println("ERROR-Card7-2");
                return;
            }
            gameMessage = (GameMessage) receivedMessage;
            chosenPawn= gameMessage.getValue();

            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn && gameController.getCurrentPlayer().getSchoolBoard().getEntrance().get(p)>=1 ){
                    count--;
                    gameController.getCurrentPlayer().getSchoolBoard().getEntrance().remove(ColourPawn.values()[chosenPawn]);
                    pawnsToRemove.add(ColourPawn.values()[chosenPawn]);
                }else{
                    valid = false;
                }
            }
        }

        /*now pawnsChosen contains the pawns chosen from the card and pawnsToRemove contains the pawns form the entrance*/
        gameController.getCurrentPlayer().getSchoolBoard().getEntrance().add(pawnsChosen);
        pawns.add(pawnsToRemove);

        //System.out.println("Your current entrance is: " + gameController.getCurrentPlayer().getSchoolBoard().getEntrance());

    }
}

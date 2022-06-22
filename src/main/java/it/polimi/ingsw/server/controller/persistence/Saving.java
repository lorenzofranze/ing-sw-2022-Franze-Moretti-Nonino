package it.polimi.ingsw.server.controller.persistence;

import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.server.controller.characters.CharacterEffect;
import it.polimi.ingsw.server.controller.logic.*;
import it.polimi.ingsw.server.controller.network.Lobby;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Saving {
    GameStatePojo gameStatePojo;

    private boolean pianificationPhaseAssistantCard;
    private boolean pianificationPhaseStudentBag;
    private HashMap<String, Integer> actionMaximumMovements = new HashMap<>();
    private List<String> actionTurnOrder = new ArrayList<>();
    private Integer actionStudentsMoved;
    private Integer actionStudentsToMove;

    private String currentPhase = null;

    private String actionResultPlayer = null;
    private boolean actionResultTowers;
    private boolean actionResultIsland;
    private boolean pianificationResultAssistantCard;
    private boolean pianificationResultStudentBag;
    private HashMap<String, Integer> pianificationResultMaximumMovements = new HashMap<>();
    private List<String> pianificationResultTurnOrder = new ArrayList<>();


    private boolean gameOver=false;

    private boolean isLastRoundFinishedAssistantCards = false;
    private boolean isLastRoundFinishedStudentsBag = false;
    private boolean isFinishedTowers = false;
    private boolean isThreeOrLessIslands = false;

    private boolean expert;
    private String currentPlayerNickname;
    private boolean forceStop;

    public Saving(GameController gameController) {
        this.gameStatePojo = gameController.getGameState();

        //saving the info of pianificationPhase
        if (gameController.getPianificationPhase() != null){
            this.pianificationPhaseAssistantCard = gameController.getPianificationPhase().isFinishedAssistantCard();
            this.pianificationPhaseStudentBag = gameController.getPianificationPhase().isFinishedStudentBag();
        }

        //saving the info of actionPhase
        if (gameController.getActionPhase() != null){
            for(Player p : gameController.getActionPhase().getMaximumMovements().keySet() ){
                this.actionMaximumMovements.put(p.getNickname(), gameController.getActionPhase().getMaximumMovements().get(p));
            }
            for(Player p : gameController.getActionPhase().getTurnOrder() ){
                this.actionTurnOrder.add(p.getNickname());
            }
        }

        if (gameController.getCurrentPhase() != null){
            this.currentPhase = gameController.getCurrentPhase().toString();
        }

        //saving the info of pianificationPhaseResult
        if (gameController.getPianificationResult() != null){
            for(Player p : gameController.getPianificationResult().getMaximumMovements().keySet() ){
                this.pianificationResultMaximumMovements.put(p.getNickname(), gameController.getPianificationResult().getMaximumMovements().get(p));
            }
            for(Player p : gameController.getPianificationResult().getTurnOrder() ){
                this.pianificationResultTurnOrder.add(p.getNickname());
            }
            this.pianificationResultAssistantCard = gameController.getPianificationResult().isFinishedAssistantCard();
            this.pianificationResultStudentBag = gameController.getPianificationResult().isFinishedStudentBag();
        }

        //saving the info of actionPhaseResult
        if (gameController.getActionResult() != null){
            this.actionResultPlayer = gameController.getActionResult().getFirstPianificationPlayer().getNickname();
            this.actionResultTowers = gameController.getActionResult().isFinishedTowers();
            this.actionResultIsland = gameController.getActionResult().isThreeOrLessIslands();
        }


        this.gameOver = gameController.isGameOver();

        this.isLastRoundFinishedAssistantCards = gameController.isLastRoundFinishedAssistantCards();
        this.isLastRoundFinishedStudentsBag = gameController.isLastRoundFinishedStudentsBag();
        this.isFinishedTowers = gameController.isFinishedTowers();
        this.isThreeOrLessIslands = gameController.isThreeOrLessIslands();

        this.expert = gameController.isExpert();

        this.currentPlayerNickname = gameController.getCurrentPlayer().getNickname();
    }
}

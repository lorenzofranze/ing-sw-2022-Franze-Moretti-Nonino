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

/**This class contains all the information needed to reload a game.
 * During a match, the gameController generates different savings' files in order to keep track of the game even if the
 * server goes down.
 * When the server is turned on and the correct players join the match, from this file a new gameController, equivalent
 * to the previous one, can be created.*/
public class Saving {

    private GameStatePojo gameStatePojo;

    private boolean pianificationPhaseAssistantCard;
    private boolean pianificationPhaseStudentBag;
    private HashMap<String, Integer> actionMaximumMovements = new HashMap<>();
    private List<String> actionTurnOrder = new ArrayList<>();
    private Integer actionStudentsMoved;
    private Integer actionStudentsToMove;
    private String actionActionResultPlayer;
    private boolean actionActionResultTowers;
    private boolean actionActionResultIslands;

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

    private RunningSection gameBookMark;

    /**creates a saving object containings the information of a specific game controller
     * @param gameController
     * */
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
            for (int i = 0; i < gameController.getActionPhase().getTurnOrder().size(); i++){
                this.actionTurnOrder.add(gameController.getActionPhase().getTurnOrder().get(i).getNickname());
            }
            if (gameController.getActionPhase().getActionResult().getFirstPianificationPlayer() != null){
                this.actionActionResultPlayer = gameController.getActionPhase().getActionResult().getFirstPianificationPlayer().getNickname();
            }
            this.actionActionResultTowers = gameController.getActionPhase().getActionResult().isFinishedTowers();
            this.actionActionResultIslands = gameController.getActionPhase().getActionResult().isThreeOrLessIslands();
        }

        if (gameController.getCurrentPhase() != null){
            this.currentPhase = gameController.getCurrentPhase().toString();
        }

        //saving the info of pianificationPhaseResult
        if (gameController.getPianificationResult() != null){
            for(Player p : gameController.getPianificationResult().getMaximumMovements().keySet() ){
                this.pianificationResultMaximumMovements.put(p.getNickname(), gameController.getPianificationResult().getMaximumMovements().get(p));
            }
            for (int i = 0; i < gameController.getPianificationResult().getTurnOrder().size(); i++){
                this.pianificationResultTurnOrder.add(gameController.getPianificationResult().getTurnOrder().get(i).getNickname());
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

        if (gameController.getGameBookMark() != null){
            this.gameBookMark = gameController.getGameBookMark();
        }

        this.gameOver = gameController.isGameOver();

        this.isLastRoundFinishedAssistantCards = gameController.isLastRoundFinishedAssistantCards();
        this.isLastRoundFinishedStudentsBag = gameController.isLastRoundFinishedStudentsBag();
        this.isFinishedTowers = gameController.isFinishedTowers();
        this.isThreeOrLessIslands = gameController.isThreeOrLessIslands();

        this.expert = gameController.isExpert();

        this.currentPlayerNickname = gameController.getCurrentPlayer().getNickname();
    }

    public GameStatePojo getGameStatePojo() {
        return gameStatePojo;
    }

    public boolean isPianificationPhaseAssistantCard() {
        return pianificationPhaseAssistantCard;
    }

    public boolean isPianificationPhaseStudentBag() {
        return pianificationPhaseStudentBag;
    }

    public HashMap<String, Integer> getActionMaximumMovements() {
        return actionMaximumMovements;
    }

    public List<String> getActionTurnOrder() {
        return actionTurnOrder;
    }

    public Integer getActionStudentsMoved() {
        return actionStudentsMoved;
    }

    public Integer getActionStudentsToMove() {
        return actionStudentsToMove;
    }

    public String getCurrentPhase() {
        return currentPhase;
    }

    public String getActionResultPlayer() {
        return actionResultPlayer;
    }

    public boolean isActionResultTowers() {
        return actionResultTowers;
    }

    public boolean isActionResultIsland() {
        return actionResultIsland;
    }

    public boolean isPianificationResultAssistantCard() {
        return pianificationResultAssistantCard;
    }

    public boolean isPianificationResultStudentBag() {
        return pianificationResultStudentBag;
    }

    public HashMap<String, Integer> getPianificationResultMaximumMovements() {
        return pianificationResultMaximumMovements;
    }

    public List<String> getPianificationResultTurnOrder() {
        return pianificationResultTurnOrder;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isLastRoundFinishedAssistantCards() {
        return isLastRoundFinishedAssistantCards;
    }

    public boolean isLastRoundFinishedStudentsBag() {
        return isLastRoundFinishedStudentsBag;
    }

    public boolean isFinishedTowers() {
        return isFinishedTowers;
    }

    public boolean isThreeOrLessIslands() {
        return isThreeOrLessIslands;
    }

    public boolean isExpert() {
        return expert;
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    public boolean isForceStop() {
        return forceStop;
    }

    public RunningSection getGameBookMark() {
        return gameBookMark;
    }

    public String getActionActionResultPlayer() {
        return actionActionResultPlayer;
    }

    public boolean isActionActionResultTowers() {
        return actionActionResultTowers;
    }

    public boolean isActionActionResultIslands() {
        return actionActionResultIslands;
    }
}

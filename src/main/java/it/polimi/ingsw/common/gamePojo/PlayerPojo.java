package it.polimi.ingsw.common.gamePojo;

import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayerPojo {

    private String nickname;
    private SchoolBoardPojo schoolBoardPojo;
    private ColourTower colourTower;
    private AssistantCardPojo playedAssistantCardPojo;
    private Set<AssistantCardPojo> deck;
    private ColourWizard wizard;
    private int coins;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public SchoolBoardPojo getSchoolBoard() {
        return schoolBoardPojo;
    }

    public void setSchoolBoard(SchoolBoardPojo schoolBoardPojo) {
        this.schoolBoardPojo = schoolBoardPojo;
    }

    public ColourTower getColourTower() {
        return colourTower;
    }

    public void setColourTower(ColourTower colourTower) {
        this.colourTower = colourTower;
    }

    public AssistantCardPojo getPlayedAssistantCard() {
        return playedAssistantCardPojo;
    }

    public void setPlayedAssistantCard(AssistantCardPojo playedAssistantCardPojo) {
        this.playedAssistantCardPojo = playedAssistantCardPojo;
    }

    public Set<AssistantCardPojo> getDeck() {
        return deck;
    }

    public void setDeck(Set<AssistantCardPojo> deck) {
        this.deck = deck;
    }

    public ColourWizard getWizard() {
        return wizard;
    }

    public void setWizard(ColourWizard wizard) {
        this.wizard = wizard;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Player getPlayer(){
        Player player = new Player(nickname, colourTower, wizard);
        player.getSchoolBoard().setEntrance(schoolBoardPojo.getEntrance().getPawnsMap());
        player.getSchoolBoard().setDiningRoom(schoolBoardPojo.getDiningRoom().getPawnsMap());
        player.getSchoolBoard().setProfessors(schoolBoardPojo.getProfessors().getPawnsMap());
        player.getSchoolBoard().setSpareTowers(schoolBoardPojo.getSpareTowers());
        player.setPlayedAssistantCard(playedAssistantCardPojo.getAssistantCard());
        player.setCoins(coins);
        Set<AssistantCard> assistantCards = new HashSet<>();
        for (AssistantCardPojo assistantCardPojo : deck){
            assistantCards.add(assistantCardPojo.getAssistantCard());
        }
        player.setDeck(assistantCards);
        return player;
    }
}

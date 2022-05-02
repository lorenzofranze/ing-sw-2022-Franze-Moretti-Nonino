package it.polimi.ingsw.common.gamePojo;

import java.util.Set;

public class Player {

    private String nickname;
    private SchoolBoard schoolBoard;
    private ColourTower colourTower;
    private AssistantCard playedAssistantCard;
    private Set<AssistantCard> deck;
    private ColourWizard wizard;
    private int coins;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public void setSchoolBoard(SchoolBoard schoolBoard) {
        this.schoolBoard = schoolBoard;
    }

    public ColourTower getColourTower() {
        return colourTower;
    }

    public void setColourTower(ColourTower colourTower) {
        this.colourTower = colourTower;
    }

    public AssistantCard getPlayedAssistantCard() {
        return playedAssistantCard;
    }

    public void setPlayedAssistantCard(AssistantCard playedAssistantCard) {
        this.playedAssistantCard = playedAssistantCard;
    }

    public Set<AssistantCard> getDeck() {
        return deck;
    }

    public void setDeck(Set<AssistantCard> deck) {
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
}

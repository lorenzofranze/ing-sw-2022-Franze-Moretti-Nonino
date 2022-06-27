package it.polimi.ingsw.common.gamePojo;

import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Player;

import java.util.*;

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
        if (playedAssistantCardPojo != null){
            player.setPlayedAssistantCard(playedAssistantCardPojo.getAssistantCard());
        }
        player.setCoins(coins);
        Set<AssistantCard> assistantCards = new HashSet<>();
        for (AssistantCardPojo assistantCardPojo : deck){
            assistantCards.add(assistantCardPojo.getAssistantCard());
        }
        player.setDeck(assistantCards);
        return player;
    }

    @Override
    public boolean equals(Object o){
        PlayerPojo o1;
        if (o == null){
            return false;
        }
        if (o instanceof PlayerPojo){
            o1 = (PlayerPojo) o;
        }else{
            return false;
        }

        if (this.coins != o1.coins){
            return false;
        }
        if (this.colourTower != o1.colourTower){
            return false;
        }
        if (this.wizard != o1.wizard){
            return false;
        }
        if (!this.nickname.equals(o1.nickname)){
            return false;
        }

        if (this.playedAssistantCardPojo != null){
            if (o1.playedAssistantCardPojo != null){
                if (!this.playedAssistantCardPojo.equals(o1.playedAssistantCardPojo)){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if (o1.playedAssistantCardPojo != null){
                return false;
            }
        }

        if (!this.schoolBoardPojo.equals(o1.schoolBoardPojo)){
            return false;
        }

        boolean found = false;
        for (AssistantCardPojo o1a : o1.deck){
            found = false;
            for (AssistantCardPojo thisa : this.deck){
                if (o1a.equals(thisa)){
                    found = true;
                }
            }
            if (found = false){
                return false;
            }
        }
        for (AssistantCardPojo thisa : this.deck){
            found = false;
            for (AssistantCardPojo o1a : o1.deck){
                if (o1a.equals(thisa)){
                    found = true;
                }
            }
            if (found = false){
                return false;
            }
        }

        return true;

    }
}

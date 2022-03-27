package it.polimi.ingsw;

import java.util.*;

public class Player {
    private final String nickName;
    private final SchoolBoard schoolBoard;
    private final ColourTower colourTower;
    private AssistantCard playedAssistantCard;
    private final Set<AssistantCard> deck;
    private final ColourWizard wizard;

    /** initializes attributes and instaces
     *  schoolboard, deck all its assistantCards**/
    public Player(String nickName, ColourTower colourTower, ColourWizard wizard){
        this.nickName = nickName;
        schoolBoard = new SchoolBoard();
        this.colourTower = colourTower;
        this.wizard = wizard;
        deck = new HashSet<>();
        AssistantCard card1 = new AssistantCard(1,1);
        AssistantCard card2 = new AssistantCard(2,1);
        AssistantCard card3 = new AssistantCard(3,2);
        AssistantCard card4 = new AssistantCard(4,2);
        AssistantCard card5 = new AssistantCard(5,3);
        AssistantCard card6 = new AssistantCard(6,3);
        AssistantCard card7 = new AssistantCard(7,4);
        AssistantCard card8 = new AssistantCard(8,4);
        AssistantCard card9 = new AssistantCard(9,5);
        AssistantCard card10 = new AssistantCard(10,5);
        deck.add(card1);
        deck.add(card2);
        deck.add(card3);
        deck.add(card4);
        deck.add(card5);
        deck.add(card6);
        deck.add(card7);
        deck.add(card8);
        deck.add(card9);
        deck.add(card10);
    }

    public String getNickName() {
        return nickName;
    }

    public ColourWizard getWizard(){
        return this.wizard;
    }

    public SchoolBoard getSchoolBoard(){
        return this.schoolBoard;
    }

    public Set<AssistantCard> getDeck() {
        return deck;
    }

    public void playAssistantCard(AssistantCard card){
        for (AssistantCard ac: deck){
            if (ac.getTurnOrder() == card.getTurnOrder())
                deck.remove(ac);
            return;
        }
    }




}

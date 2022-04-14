package it.polimi.ingsw.server.Model;

import java.util.*;

/* tolti riferimenti a controller perchè
* se è in modalità complex nella setup phase
* inremento direttamente di 1 il numero di monete
* di ogni player
 */
public class Player {

    //SIMPLE GAME ATTRIBUTES
    private final String nickname;
    private final SchoolBoard schoolBoard;
    private final ColourTower colourTower;
    private AssistantCard playedAssistantCard;
    private final Set<AssistantCard> deck;
    private final ColourWizard wizard;
    private boolean connected;

    //COMPLEX GAME ATTRIBUTES
    private int coins;


    //SIMPLE GAME METHODS

    /** initializes attributes and instaces
     *  schoolboard, deck all its assistantCards. Gives 0 coins to the player**/
    public Player(String nickname, ColourTower colourTower, ColourWizard wizard){

        this.nickname = nickname;
        schoolBoard = new SchoolBoard();
        this.colourTower = colourTower;
        this.wizard = wizard;
        this.coins=0;
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


    public String getNickname() {
        return nickname;
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

    /** select assistnt card with the card order in input */
    public void playAssistantCard(int cardOrder){
        for (AssistantCard ac: deck){
            if (ac.getTurnOrder() == cardOrder){
                deck.remove(ac);
                this.playedAssistantCard = ac;
                return;
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /** get assistant card played */
    public AssistantCard getPlayedAssistantCard() {
        return playedAssistantCard;
    }

    /** reset assistant card attribute */
    public void resetAssistantCard(){
        this.playedAssistantCard = null;
    }


    //COMPLEX GAME METHODS

    /** return the number of coins */
    public int getCoins() {
        return coins;
    }

    /** add coins, can be used to set 1 coin in complex mode   **/
    public void addCoins(int coins) {
        this.coins += coins;
    }

    /** decrement coins */
    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    /** get colour tower of the player */
    public ColourTower getColourTower() {
        return colourTower;
    }

    @Override
    public String toString(){
        return this.nickname;
    }

}

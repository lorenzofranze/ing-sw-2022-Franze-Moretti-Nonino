package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.*;

import java.util.*;

/**
 * This class contains information (and the related method to manage them) related to the player:
 * the nickname the assistant card played in the pianification phase,
 * the coins he owns, the remaining assistant card in his deck and the colour of its tower.
 */
public class Player {
    private final String nickname;
    private final SchoolBoard schoolBoard;
    private final ColourTower colourTower;
    private AssistantCard playedAssistantCard;
    private Set<AssistantCard> deck;
    private final ColourWizard wizard;
    private int coins;

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

    /** select assistant card with the card order in input */
    public void playAssistantCard(int cardOrder){
        for (AssistantCard ac: deck){
            if (ac.getTurnOrder() == cardOrder){
                deck.remove(ac);
                this.playedAssistantCard = ac;
                return;
            }
        }
    }

    /** get assistant card played */
    public AssistantCard getPlayedAssistantCard() {
        return playedAssistantCard;
    }

    /** reset assistant card attribute */
    public void resetAssistantCard(){
        this.playedAssistantCard = null;
    }

    public void setPlayedAssistantCard(AssistantCard playedAssistantCard) {
        this.playedAssistantCard = playedAssistantCard;
    }

    /** return the number of coins */
    public int getCoins() {
        return coins;
    }

    /** add coins, can be used to set 1 coin in complex mode **/
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

    @Override
    public String toString(){
        return this.nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return nickname.equals(player.nickname);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }


    /**returns a PlayerPojo representing this player*/
    public PlayerPojo toPojo(){
        PlayerPojo pojoPlayerPojo = new PlayerPojo();
        pojoPlayerPojo.setNickname(this.getNickname());
        pojoPlayerPojo.setCoins(this.getCoins());
        pojoPlayerPojo.setColourTower(this.getColourTower());
        pojoPlayerPojo.setWizard(this.getWizard());

        SchoolBoardPojo pojoSchoolBoardPojo = new SchoolBoardPojo();
        PawnsMapPojo pojoDiningRoom = new PawnsMapPojo(this.getSchoolBoard().getDiningRoom());
        pojoSchoolBoardPojo.setDiningRoom(pojoDiningRoom);
        PawnsMapPojo pojoEntrance = new PawnsMapPojo(this.getSchoolBoard().getEntrance());
        pojoSchoolBoardPojo.setEntrance(pojoEntrance);
        PawnsMapPojo pojoProfessors = new PawnsMapPojo(this.getSchoolBoard().getProfessors());
        pojoSchoolBoardPojo.setProfessors(pojoProfessors);
        pojoSchoolBoardPojo.setSpareTowers(this.getSchoolBoard().getSpareTowers());
        pojoPlayerPojo.setSchoolBoard(pojoSchoolBoardPojo);

        Set<AssistantCardPojo> deck  = new HashSet<>();
        for (AssistantCard a : this.getDeck()){
            AssistantCardPojo pojoAssistantCardPojo = a.toPojo();
            deck.add(pojoAssistantCardPojo);
        }
        pojoPlayerPojo.setDeck(deck);
        if (this.getPlayedAssistantCard() != null){
            it.polimi.ingsw.common.gamePojo.AssistantCardPojo pojo = this.getPlayedAssistantCard().toPojo();
            pojoPlayerPojo.setPlayedAssistantCard(pojo);
        }

        return pojoPlayerPojo;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setDeck(Set<AssistantCard> deck) {
        this.deck = deck;
    }
}

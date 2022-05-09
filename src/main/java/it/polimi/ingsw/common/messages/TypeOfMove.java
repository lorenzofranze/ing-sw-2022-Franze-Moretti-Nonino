package it.polimi.ingsw.common.messages;

public enum TypeOfMove {
    AssistantCard(0), StudentColour(1), StudentMovement(2), MoveMotherNature(3),
    CharacterCard(4),CloudChoice(5), IslandChoice(6), StudentNumber(7);

    private int messageCode;
    private TypeOfMove(int i) {
        messageCode=i;
    }
}

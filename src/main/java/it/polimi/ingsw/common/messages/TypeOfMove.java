package it.polimi.ingsw.common.messages;

/**
 * Enum for GameMessage and PawnMovementMessage
 */
public enum TypeOfMove {
    AssistantCard(0), StudentColour(1), PawnMovement(2), MoveMotherNature(3),
    CharacterCard(4),CloudChoice(5), IslandChoice(6), StudentNumber(7), NumOfMove(8);

    private int messageCode;
    private TypeOfMove(int i) {
        messageCode=i;
    }
}

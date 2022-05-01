package it.polimi.ingsw.common.messages;

public enum TypeOfMessage {
    AssistantCard(1), StudentColour(2), MoveMotherNature(3),
    CharacterCard(4),CloudChoice(5) , Connection(6),
    GameState(7), IslandChoice(8), Where(10),
    Number(11), ErrorMessage(12), StringMessage(13);

    private int messageCode;

    private TypeOfMessage(int i) {
        messageCode=i;
    }
}

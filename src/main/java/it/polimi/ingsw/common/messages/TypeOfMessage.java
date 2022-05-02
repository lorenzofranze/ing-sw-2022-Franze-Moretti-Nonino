package it.polimi.ingsw.common.messages;

public enum TypeOfMessage {
    AssistantCard(1), StudentColour(2), MoveMotherNature(3),
    CharacterCard(4),CloudChoice(5) , Connection(6),
    Update(7), IslandChoice(8), Where(9),
    Number(10), ErrorMessage(11), StringMessage(13);

    private int messageCode;

    private TypeOfMessage(int i) {
        messageCode=i;
    }
}

package it.polimi.ingsw.common.messages;

public enum TypeOfMessage {

    Connection(0), AssistantCard(1), StudentColour(2), StudentPosition(3), MoveMotherNature(3),
    CharacterCard(4),CloudChoice(5), IslandChoice(6), Update(7), EndGame(8), ErrorMessage(9),
    StringMessage(10), Disconnection(11), Number(12);

    private int messageCode;

    private TypeOfMessage(int i) {
        messageCode=i;
    }
}

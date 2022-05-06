package it.polimi.ingsw.common.messages;

public enum TypeOfMessage {

    Connection(0), AssistantCard(1), StudentColour(2), StudentMovement(3), MoveMotherNature(3),
    CharacterCard(4),CloudChoice(5), IslandChoice(6), Update(7), EndGame(8), Error(9),
    String(10), Async(11), Number(12), Ping(13);

    private int messageCode;

    private TypeOfMessage(int i) {
        messageCode=i;
    }
}

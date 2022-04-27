package it.polimi.ingsw.Server.Controller.Network.Messages;

public enum TypeOfMessage {
    AssistantCard(1), StudentColour(2), MoveMotherNature(3),
    CharacterCard(4),CloudChoice(5) , Connection(6),
    GameState(7), IslandChoice(8), Where(10),
    Number(11), Ping(12), GameStateACK(13);
    private int numOfMove;

    private TypeOfMessage(int i) {
        numOfMove=i;
    }
}

package it.polimi.ingsw.Server.Controller.Network.Messages;

public enum TypeOfMessage {
    AssistantCard(1), StudentToMove(2), MoveMotherNature(3), CharacterCard(4),CloudChoice(5) , Connection(6), GameState(7), IslandChoice3(8), StudentChoice(9);
    private int numOfMove;

    private TypeOfMessage(int i) {
        numOfMove=i;
    }
}

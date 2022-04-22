package it.polimi.ingsw.Server.Controller.Network.Messages;

public enum TypeOfMessage {
    AssistantCard(1), StudentToMove(2), MoveMotherNature(3), CharacterCard(4),CloudChoice(5) , Connection(6), ActionMove(5), ChooseCloudMove(3);
    private int numOfMove;

    private TypeOfMessage(int i) {
        numOfMove=i;
    }
}

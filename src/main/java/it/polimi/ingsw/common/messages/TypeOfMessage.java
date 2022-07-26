package it.polimi.ingsw.common.messages;

/**
 * Enum for all the messages
 */
public enum TypeOfMessage {

    Connection(0), Ack(1), Update(2), Game(3), Error(4), Ping(5), Async(6), Pong(7);

    private int messageCode;
    private TypeOfMessage(int i) {
        messageCode=i;
    }
}

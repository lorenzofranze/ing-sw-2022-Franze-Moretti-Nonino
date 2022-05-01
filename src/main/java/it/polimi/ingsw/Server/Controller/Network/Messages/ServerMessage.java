package it.polimi.ingsw.Server.Controller.Network.Messages;


public class ServerMessage extends Message{

    public ServerMessage(TypeOfMessage typeOfMessage){
        super.setMessageType(typeOfMessage);
    }

}

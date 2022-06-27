package it.polimi.ingsw.common.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.persistence.Saving;
import it.polimi.ingsw.server.controller.persistence.SavingsMenu;
import it.polimi.ingsw.server.model.CharacterState;

public class  JsonConverter {
    private static GsonBuilder builder = new GsonBuilder();
    private static Gson gson;

    static{
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    /**
     * Convert a JsonString in a Message of the correct type accordinf to rhe type of message.
     * Precisesly:
     * first the string it is converted to message so that the message type con be explored,
     * then the string is converted into a message of a specific type
     * @param jsonString
     * @return
     */
    public static Message fromJsonToMessage(String jsonString){

        Message message = gson.fromJson(jsonString, Message.class);

        if(message==null) return null;
        if(message.getMessageType() == TypeOfMessage.Connection){
            ConnectionMessage messageReal=  gson.fromJson(jsonString, ConnectionMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Ack){
            AckMessage messageReal=  gson.fromJson(jsonString, AckMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Update){
            UpdateMessage messageReal=  gson.fromJson(jsonString, UpdateMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Game){
            GameMessage messageReal1 =  gson.fromJson(jsonString, GameMessage.class);
            if (messageReal1.getTypeOfMove() == TypeOfMove.PawnMovement){
                PawnMovementMessage messageReal2 = gson.fromJson(jsonString, PawnMovementMessage.class);
                return messageReal2;
            }
            return messageReal1;
        }

        else if(message.getMessageType() == TypeOfMessage.Error){
            ErrorMessage messageReal=  gson.fromJson(jsonString, ErrorMessage.class);
            return messageReal;
        }

        else if(message.getMessageType() == TypeOfMessage.Ping){
            PingMessage messageReal=  gson.fromJson(jsonString, PingMessage.class);
            return messageReal;
        }

        else if(message.getMessageType() == TypeOfMessage.Pong){
            PongMessage messageReal=  gson.fromJson(jsonString, PongMessage.class);
            return messageReal;
        }

        else if(message.getMessageType() == TypeOfMessage.Async){
            AsyncMessage messageReal=  gson.fromJson(jsonString, AsyncMessage.class);
            return messageReal;
        }

        return message;
    }


    /**
     * Convert a Message into a JsonString.
     **/
    public static String fromMessageToJson(Message message){
        String jsonString=gson.toJson(message) + "\nEOF\n";
        return(jsonString);
    }

    public static String fromSavingToJson(Saving saving){
        String jsonString=gson.toJson(saving);
        return(jsonString);
    }

    public static Saving fromJsonToSaving(String jsonString){
        Saving saving = gson.fromJson(jsonString, Saving.class);
        return saving;
    }

    public static String fromSavingsMenuToJson(SavingsMenu savingsMenu){
        String jsonString=gson.toJson(savingsMenu);
        return(jsonString);
    }

    public static SavingsMenu fromJsonToSavingsMenu(String jsonString){
        SavingsMenu savingsMenu = gson.fromJson(jsonString, SavingsMenu.class);
        return savingsMenu;
    }


}

package it.polimi.ingsw.common.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class  JsonConverter {
    private static GsonBuilder builder = new GsonBuilder();
    private static Gson gson;

    static{
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public static Message fromJsonToMessage(String jsonString){
        Message message = gson.fromJson(jsonString, Message.class);

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
            GameMessage messageReal=  gson.fromJson(jsonString, GameMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Error){
            ErrorMessage messageReal=  gson.fromJson(jsonString, ErrorMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Ping){
            PingMessage messageReal=  gson.fromJson(jsonString, PingMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Async){
            AsyncMessage messageReal=  gson.fromJson(jsonString, AsyncMessage.class);
            return messageReal;
        }

        return message;
    }


    public static String fromMessageToJson(Message message){
        String jsonString=gson.toJson(message) + "\nEOF\n";
        return(jsonString);
    }

}

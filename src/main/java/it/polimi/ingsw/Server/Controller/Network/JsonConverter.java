package it.polimi.ingsw.Server.Controller.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Server.Controller.Network.Messages.ConnectionMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.Message;

public class  JsonConverter {
    private static GsonBuilder builder = new GsonBuilder();
    private static Gson gson;

    static{
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public static Message fromJsonToMessage(String jsonString){
        Message message = gson.fromJson(jsonString, Message.class);
        return message;
    }


    public static String fromMessageToJson(Message message){
        String jsonString=gson.toJson(message);
        return(jsonString);
    }

}

package it.polimi.ingsw.server.Controller.Messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonConverter {
    private GsonBuilder builder;
    private Gson gson;

    public JsonConverter(){
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }
    public Message fromJsonToMessage(String jsonString){
        Message message = gson.fromJson(jsonString, Message.class);
        return message;
    }


    public String fromMessageToJson(Message message){
        String jsonString=this.gson.toJson(message);
        return(jsonString);
    }
}

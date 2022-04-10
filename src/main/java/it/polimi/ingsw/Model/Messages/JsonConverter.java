package it.polimi.ingsw.Model.Messages;

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
    public ClientMessage fromJsonToMessage(String jsonString){
        ClientMessage message = gson.fromJson(jsonString, MoveStudentMessage.class);
        return message;
    }


    public String fromMessageToJson(){
        String jsonString=this.gson.toJson(this);
        return(jsonString);
    }
}

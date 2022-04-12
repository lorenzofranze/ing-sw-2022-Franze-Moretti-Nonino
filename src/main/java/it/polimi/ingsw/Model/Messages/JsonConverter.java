package it.polimi.ingsw.Model.Messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonConverter {
    private static JsonConverter jsonConverter;
    private GsonBuilder builder;
    private Gson gson;

    private JsonConverter(){
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public static JsonConverter getGsonIstance() {
        if(jsonConverter==null) {
            jsonConverter=new JsonConverter();
        }
        return jsonConverter;
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

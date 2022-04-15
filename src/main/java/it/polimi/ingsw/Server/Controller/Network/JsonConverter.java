package it.polimi.ingsw.Server.Controller.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Server.Controller.Network.Messages.Message;

public class JsonConverter {
    private static JsonConverter jsonConverter;
    private GsonBuilder builder;
    private Gson gson;

    public JsonConverter(){
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public static JsonConverter getJsonConverterInstance() {
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

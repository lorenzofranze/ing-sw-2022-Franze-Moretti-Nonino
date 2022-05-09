package it.polimi.ingsw.common.messages;

public class AsyncMessage extends Message{
    private TypeOfAsync typeOfAsync;
    private String description;

    public AsyncMessage(TypeOfAsync typeOfAsync, String description){
        super(TypeOfMessage.Async);
        this.typeOfAsync = typeOfAsync;
        this.description = description;
    }

    public AsyncMessage(TypeOfAsync typeOfAsync){
        super(TypeOfMessage.Async);
        this.typeOfAsync = typeOfAsync;
        this.description = "";
    }

    public String getDescription() {
        return description;
    }
    public TypeOfAsync getTypeOfAsync() {
        return typeOfAsync;
    }
}

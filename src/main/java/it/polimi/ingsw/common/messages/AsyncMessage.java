package it.polimi.ingsw.common.messages;

/**
 * Messages for disconnections
 */
public class AsyncMessage extends Message{
    private String description;

    public AsyncMessage(String description){
        super(TypeOfMessage.Async);
        this.description = description;
    }

    public AsyncMessage(){
        super(TypeOfMessage.Async);
        this.description = "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

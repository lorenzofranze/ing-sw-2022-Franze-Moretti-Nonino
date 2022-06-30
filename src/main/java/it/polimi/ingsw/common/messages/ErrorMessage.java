package it.polimi.ingsw.common.messages;

/**
 * Messages for errors
 */
public class ErrorMessage extends Message{
    private TypeOfError typeOfError;
    private String description;

    public ErrorMessage(TypeOfError typeOfError, String description){
        super(TypeOfMessage.Error);
        this.typeOfError = typeOfError;
        this.description = description;
    }

    public ErrorMessage(TypeOfError typeOfError){
        super(TypeOfMessage.Error);
        this.typeOfError = typeOfError;
        this.description = "";
    }

    public String getDescription() {
        return description;
    }
    public TypeOfError getTypeOfError() {
        return typeOfError;
    }
}

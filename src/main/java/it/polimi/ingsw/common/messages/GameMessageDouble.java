package it.polimi.ingsw.common.messages;

public class GameMessageDouble extends GameMessage{
    private int valueDouble;


    public GameMessageDouble(int a, int b){
        super(TypeOfMessage.StudentMovement, a);
        this.valueDouble=b;
    }

    public int getValueDouble(){
        return valueDouble;
    }
}

package it.polimi.ingsw.common.messages;

public class GameMessageDouble extends GameMessage{
    int[] value;


    public GameMessageDouble(int a, int b){
        super(TypeOfMessage.StudentMovement, -1);
        value[0]=a;   value[1]=b;
    }

    public int[] getValueDouble(){
        return value;
    }
}

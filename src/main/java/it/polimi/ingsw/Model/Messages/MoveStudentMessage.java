package it.polimi.ingsw.Model.Messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Model.PawnsMap;

public class MoveStudentMessage implements Message{
    private PawnsMap studentsToMove;
    private Integer where;
    private String nickname;
}

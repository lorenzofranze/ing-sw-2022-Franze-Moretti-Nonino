package it.polimi.ingsw.server.controller.logic;

/**
 * Class used in gameController-class in the field "currentPhase" is of type GamePhase
 */
public abstract class GamePhase {

    @Override
    public boolean equals(Object o){

        ActionPhase this1;
        PianificationPhase this2;
        SetUpPhase this3;

        if (o == null){
            return false;
        }
        if (this instanceof ActionPhase){
            this1 = (ActionPhase) this;
            return this1.equals(o);
        }
        if (this instanceof PianificationPhase){
            this2 = (PianificationPhase) this;
            return this2.equals(o);
        }
        if (this instanceof SetUpPhase){
            this3 = (SetUpPhase) this;
            return this3.equals(o);
        }
        return false;

    }
}

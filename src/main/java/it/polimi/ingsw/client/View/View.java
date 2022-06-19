package it.polimi.ingsw.client.View;

import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.gamePojo.PlayerPojo;
import it.polimi.ingsw.common.messages.*;
import javafx.stage.Stage;

public interface View{


    void beginReadUsername();
    void chooseGameMode();

    void chooseAssistantCard();
    void askForCharacter();
    void moveStudent();
    void placeMotherNature();
    void chooseCloud();

    void showMessage(Message message);
    void showConnection(ConnectionMessage connectionMessage);
    void showError(ErrorMessage errorMessage);
    void showAck(AckMessage ackMessage);
    void showUpdate(UpdateMessage updateMessage);
    void showAsync(AsyncMessage asyncMessage);
    void showPing(PingMessage pingMessage);
    void showMove(GameMessage gameMessage);
    void showGameState(GameStatePojo gameStatePojo);

    //FOR COMPLEX MODE:
    void moveStudentToIsland(); //used from card 1
    void chooseColour(); //used from cards 9 - 12 - 7 - 10 - 11
    void chooseIsland(); //used from cards 3 - 5
    void chooseNumOfMove();

    //used only by gui
    void showEffect(int num);
    void canMoveCoin(boolean b);

    void skipAskForCharacterGUI(boolean b);
}

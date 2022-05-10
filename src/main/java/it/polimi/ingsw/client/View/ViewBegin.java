package it.polimi.ingsw.client.View;

import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;

public interface ViewBegin {

    void setViewEnd(ViewEnd viewEnd);
    void beginReadUsername();
    void chooseGameMode();

    void showMessage(Message message);
    void showConnection(ConnectionMessage connectionMessage);
    void showError(ErrorMessage errorMessage);
    void showAck(AckMessage ackMessage);
    void showUpdate(UpdateMessage updateMessage);
    void showAsync(AsyncMessage asyncMessage);
    void showPing(PingMessage pingMessage);
    void showMove(GameMessage gameMessage);

    void showGameState(GameStatePojo gameStatePojo);
}

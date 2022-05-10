package it.polimi.ingsw.client.View;

import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;

public interface ViewEnd {
    void endReadUsername(String username);
    void endChooseGameMode(int gameMode);

    void endShowMessage(Message message);
    void endShowConnection(ConnectionMessage connectionMessage);
    void endShowError(ErrorMessage errorMessage);
    void endShowAck(AckMessage ackMessage);
    void endShowUpdate(UpdateMessage updateMessage);
    void endShowPing(PingMessage pingMessage);
    void endShowAsync(AsyncMessage asyncMessage);
    void endShowMove(GameMessage gameMessage);

    void endShowGameState(GameStatePojo gameStatePojo);
}

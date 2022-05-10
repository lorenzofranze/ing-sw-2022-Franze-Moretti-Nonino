package it.polimi.ingsw.client.View;

import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;

public class CLIViewEnd implements ViewEnd{
    @Override
    public void endReadUsername(String username){};
    @Override
    public void endChooseGameMode(int gameMode){};

    @Override
    public void endChooseAssistantCard() {}

    @Override
    public void endShowMessage(Message message) {}
    @Override
    public void endShowConnection(ConnectionMessage connectionMessage) {}
    @Override
    public void endShowError(ErrorMessage errorMessage) {}
    @Override
    public void endShowAck(AckMessage ackMessage) {}
    @Override
    public void endShowUpdate(UpdateMessage updateMessage) {}
    @Override
    public void endShowAsync(AsyncMessage asyncMessage) {}
    @Override
    public void endShowPing(PingMessage pingMessage) {}
    @Override
    public void endShowMove(GameMessage gameMessage) {}

    @Override
    public void endShowGameState(GameStatePojo gameStatePojo){}
}

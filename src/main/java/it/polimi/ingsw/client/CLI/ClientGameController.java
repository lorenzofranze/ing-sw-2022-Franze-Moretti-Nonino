package it.polimi.ingsw.client.CLI;


import it.polimi.ingsw.common.gamePojo.GameState;
import it.polimi.ingsw.common.messages.JsonConverter;
import it.polimi.ingsw.common.messages.UpdateMessage;

import java.io.IOException;

public class ClientGameController {
    private LineClient lineClient;

    public ClientGameController(LineClient lineClient){
        this.lineClient=lineClient;
    }

    public void play() throws IOException {
        GameState gamestate;
        do {
            UpdateMessage updateMessage = (UpdateMessage) JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
            gamestate = updateMessage.getGameState();
            gamestate.show();
        }while(!gamestate.isGameOver());

    }
}

package it.polimi.ingsw.client.CLI;


import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.gamePojo.Phase;
import it.polimi.ingsw.common.messages.*;

import java.io.IOException;
import java.util.Scanner;

public class ClientGameController {
    private LineClient lineClient;

    public ClientGameController(LineClient lineClient){
        this.lineClient=lineClient;
    }

    public void play() throws IOException {
        Scanner scanner = new Scanner(System.in);
        GameStatePojo gamestate;
        UpdateMessage updateMessage;
        int chosen;
        boolean valid;
        do {
            do {
                updateMessage = (UpdateMessage) JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                gamestate = updateMessage.getGameState();
                ShowGame.show(gamestate);

                //è il mio turno di pianificazione
                if (gamestate.getCurrentPhase() == Phase.PIANIFICATION && gamestate.getCurrentPlayer().equals(lineClient.getNickname())) {
                    System.out.println("scegliere carta assistente:");
                    do {
                        valid = true;
                        chosen = lineClient.readInt(lineClient.getScanner());
                        GameMessage gm = new GameMessage(TypeOfMessage.AssistantCard, chosen);
                        String stringToSend = JsonConverter.fromMessageToJson(gm);
                        try {
                            lineClient.getOut().write(stringToSend);
                            lineClient.getOut().flush();
                        } catch (IOException e) {
                            System.out.println("impossibile inviare il messaggio: disconnesso");
                            throw e;
                        }

                        Message response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                        if (response.getMessageType() == TypeOfMessage.Error) {
                            valid = false;
                            if (((GameErrorMessage) response).getError() == 5) {
                                System.out.println("an other player has alrealy played this card in this round, rechoose");
                            } else if (((GameErrorMessage) response).getError() == 6) {
                                System.out.println("You have already played this card or invalid chooise");
                            }
                        } else {
                            updateMessage = (UpdateMessage) JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                            gamestate = updateMessage.getGameState();
                            ShowGame.show(gamestate);
                        }
                    } while (!valid);
                }
            }while(gamestate.getCurrentPhase()==Phase.PIANIFICATION);

            do{
                updateMessage = (UpdateMessage) JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                gamestate = updateMessage.getGameState();
                ShowGame.show(gamestate);

                // è il mio turno di action
                if(gamestate.getCurrentPhase() == Phase.ACTION && gamestate.getCurrentPlayer().equals(lineClient.getNickname())){

                }


            }while(gamestate.getCurrentPhase()==Phase.ACTION);

            updateMessage = (UpdateMessage) JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
            gamestate = updateMessage.getGameState();
            ShowGame.show(gamestate);

        }while(!gamestate.isGameOver());


    }
}

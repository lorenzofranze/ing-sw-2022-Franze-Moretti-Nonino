package it.polimi.ingsw.client.CLI;


import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.gamePojo.Phase;
import it.polimi.ingsw.common.messages.*;

import java.io.IOException;
import java.util.Scanner;

public class ClientGameController implements Runnable {
    private LineClient lineClient;

    public ClientGameController(LineClient lineClient){
        this.lineClient=lineClient;
    }


    public void run ()  {
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
                    //TODO: timer per notificare che ci sta mettendo troppo;
                    //Thread timer = new Thread();
                    //timer.start();
                    do {
                        System.out.println("choose assistant card:");
                        valid = true;
                        chosen = lineClient.readInt(lineClient.getScanner());
                        GameMessage gm = new GameMessage(TypeOfMessage.AssistantCard, chosen);
                        String stringToSend = JsonConverter.fromMessageToJson(gm);
                        try {
                            lineClient.getOut().write(stringToSend);
                            lineClient.getOut().flush();
                        } catch (IOException e) {
                            System.out.println("impossibile inviare il messaggio: disconnesso");
                            lineClient.endClient();
                            return;
                        }

                        Message response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                        if (response.getMessageType() == TypeOfMessage.Error) {
                            valid = false;
                            if (((GameErrorMessage) response).getError() == ErrorStatusCode.RULESVIOLATION_1) {
                                System.out.println("an other player has alrealy played this card in this round, rechoose");
                            } else if (((GameErrorMessage) response).getError() == ErrorStatusCode.INDEXINVALID) {
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
                    System.out.println("MY TURN");
                    // MOVE students
                    do{
                        valid=true;
                        this.printStudentRequest( gamestate);
                        chosen = lineClient.readInt(lineClient.getScanner());
                        // the player uses character card
                        if(chosen>=11 && chosen<=13){
                            try {
                                valid = useCharacterCard(chosen);
                                if(valid){
                                    //gestire varie carte personaggio... meglio in altro metodo
                                }
                            }catch(IOException ex ){
                                ex.printStackTrace();
                                return;
                            }
                        }
                        // the player move 3 or 4 students
                        else{
                            for(int i=0; i<gamestate.getPlayers().size()-1; i++){
                                boolean correctMovement;
                                do {
                                    correctMovement=true;
                                    GameMessage gm = new GameMessage(TypeOfMessage.StudentColour, chosen);
                                    String stringToSend = JsonConverter.fromMessageToJson(gm);
                                    try {
                                        lineClient.getOut().write(stringToSend);
                                        lineClient.getOut().flush();
                                    } catch (IOException e) {
                                        System.out.println("impossibile inviare il messaggio: disconnesso");
                                        lineClient.endClient();
                                        return;
                                    }

                                    Message response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                                    if (response.getMessageType() == TypeOfMessage.Error) {
                                        valid = false;
                                        if (((GameErrorMessage) response).getError() == ErrorStatusCode.INDEXINVALID) {
                                            System.out.println("colour invalid");
                                        } else if (((GameErrorMessage) response).getError() == ErrorStatusCode.RULESVIOLATION_1) {
                                            System.out.println("you don't have that colour");
                                        } // continuare
                                    } else {
                                        updateMessage = (UpdateMessage) JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                                        gamestate = updateMessage.getGameState();
                                        ShowGame.show(gamestate);
                                    }
                                }while(!correctMovement);
                            }
                        }

                    }while(!valid);
                }


            }while(gamestate.getCurrentPhase()==Phase.ACTION);

            updateMessage = (UpdateMessage) JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
            gamestate = updateMessage.getGameState();
            ShowGame.show(gamestate);

        }while(!gamestate.isGameOver());

    }

    private void printRequest(String toPrint, GameStatePojo gameState){
        if(gameState.isExpert()){
            System.out.println(toPrint+" (11 / 12 / 13 for character cards)");
            return;
        }
        System.out.println(toPrint);
    }

    private void printStudentRequest(GameStatePojo gameState){
        String toPrint = "move ";
        toPrint = toPrint + (gameState.getPlayers().size()==2 ? "3" : "4");
        toPrint += "students";
        if(gameState.isExpert()){
            System.out.println(toPrint+" (11 / 12 / 13 for character cards)");
            return;
        }
    }

    private boolean useCharacterCard(int chosen) throws IOException{
        boolean valid = true;
        GameStatePojo gamestate;
        UpdateMessage updateMessage;
        GameMessage gm = new GameMessage(TypeOfMessage.CharacterCard, chosen-10);
        String stringToSend = JsonConverter.fromMessageToJson(gm);
        try {
            lineClient.getOut().write(stringToSend);
            lineClient.getOut().flush();
        } catch (IOException e) {
            System.out.println("impossibile inviare il messaggio: disconnesso");
            lineClient.endClient();
            throw new IOException();
        }
        Message response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
        if (response.getMessageType() == TypeOfMessage.Error) {
            valid = false;
            if (((GameErrorMessage) response).getError() == ErrorStatusCode.INDEXINVALID) {
                System.out.println("index invalid");
            } else if (((GameErrorMessage) response).getError() == ErrorStatusCode.RULESVIOLATION_1) {
                System.out.println("there is an other effect activated");
            } else if (((GameErrorMessage) response).getError() == ErrorStatusCode.RULESVIOLATION_2){
                System.out.println("you don't have enought money");
            }
        } else {
            updateMessage = (UpdateMessage) JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
            gamestate = updateMessage.getGameState();
            ShowGame.show(gamestate);
        }
        return valid;
    }
}

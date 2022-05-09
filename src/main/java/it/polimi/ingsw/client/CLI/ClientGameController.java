package it.polimi.ingsw.client.CLI;


import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.gamePojo.Phase;
import it.polimi.ingsw.common.messages.*;

import java.io.IOException;
import java.util.Scanner;

public class ClientGameController implements Runnable {
    private LineClient lineClient;

    public ClientGameController(LineClient lineClient) {
        this.lineClient = lineClient;
    }


    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        GameStatePojo gamestate;
        UpdateMessage updateMessage;
        int chosen;
        boolean valid;
        boolean moveMade;
        Message response;
        do {
            do {
                gamestate = receiveUpdate();
                if (gamestate== null) {
                    System.out.println("gamestate = null");
                    return;
                }

                //è il mio turno di pianificazione
                if (gamestate.getCurrentPhase() == Phase.PIANIFICATION && gamestate.getCurrentPlayer().getNickname().equals(lineClient.getNickname())) {
                    //TODO: timer per notificare che ci sta mettendo troppo;
                    //Thread timer = new Thread();
                    //timer.start();
                    do {
                        System.out.println("choose assistant card:");
                        valid = true;
                        chosen = VIEWclientCLI.readInt();
                        GameMessage gm = new GameMessage(TypeOfMove.AssistantCard, chosen);
                        String stringToSend = JsonConverter.fromMessageToJson(gm);
                        try {
                            lineClient.getOut().write(stringToSend);
                            lineClient.getOut().flush();
                        } catch (IOException e) {
                            System.out.println("impossible to send the message: disconnected");
                            lineClient.endClient();
                            return;
                        }
                        try {
                            response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                        } catch (IOException ex) {
                            System.out.println("impossibile to read the message: disconnected");
                            return;
                        }
                        if (response.getMessageType() == TypeOfMessage.Error) {
                            valid = false;
                            ErrorMessage responseError = (ErrorMessage) response;
                            if (responseError.getTypeOfError().equals(TypeOfError.InvalidChoice)) {
                                System.out.println("InvalidChoice");
                            } else {
                                System.out.println("ERROR - typeOfError received: " + responseError.getTypeOfError());
                            }
                        } else {
                            gamestate = receiveUpdate();
                        }
                    } while (!valid);
                }
            } while (gamestate.getCurrentPhase() == Phase.PIANIFICATION);

            do {
                gamestate = receiveUpdate();
                // è il mio turno di action
                if (gamestate.getCurrentPhase() == Phase.ACTION && gamestate.getCurrentPlayer().getNickname().equals(lineClient.getNickname())) {
                    System.out.println("MY TURN");
                    // MOVE students
                    for (int i = 0; i < gamestate.getPlayers().size() + 1; i++) {
                        do {
                            valid = true;
                            moveMade = false;
                            this.printStudentRequest(gamestate);
                            chosen = VIEWclientCLI.readInt();
                            // the player uses character card
                            if (chosen >= 11 && chosen <= 13) {
                                try {
                                    valid = useCharacterCard(chosen);
                                    if (valid) {
                                        //gestire varie carte personaggio... meglio in altro metodo
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    return;
                                }
                            }
                            // the player move 3 or 4 students
                            else {
                                int chosenPosition;
                                System.out.println("choose destination (-1 for school board)");
                                chosenPosition = VIEWclientCLI.readInt();
                                GameMessage gm = new GameMessageDouble(chosen, chosenPosition);
                                String stringToSend = JsonConverter.fromMessageToJson(gm);
                                try {
                                    lineClient.getOut().write(stringToSend);
                                    lineClient.getOut().flush();
                                } catch (IOException e) {
                                    System.out.println("impossible to send the message: disconnected");
                                    lineClient.endClient();
                                    return;
                                }
                                try {
                                    response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                                } catch (IOException ex) {
                                    System.out.println("impossibile to read the message: disconnected");
                                    return;
                                }
                                if (response.getMessageType() == TypeOfMessage.Error) {
                                    valid = false;
                                    ErrorMessage responseError = (ErrorMessage) response;
                                    if (responseError.getTypeOfError().equals(TypeOfError.InvalidChoice)) {
                                        System.out.println("invalid choice");
                                    } else if (responseError.getTypeOfError().equals(TypeOfError.FullDiningRoom)) {
                                        System.out.println("out of row on school board: more than 10 students");
                                    } else
                                        System.out.println("ERROR - typeOfError received: "+responseError.getTypeOfError());
                                } else {
                                    gamestate = receiveUpdate();
                                    moveMade = true;
                                }
                            }
                        } while (!valid || !moveMade);
                    } // end move student phase

                    // MOVE MOTHER NATURE
                    do {
                        valid = true;
                        moveMade = false;
                        this.printRequest(" mother nature step: ", gamestate);
                        chosen = VIEWclientCLI.readInt();
                        // the player uses character card
                        if (chosen >= 11 && chosen <= 13) {
                            try {
                                valid = useCharacterCard(chosen);
                                if (valid) {
                                    //gestire varie carte personaggio... meglio in altro metodo
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                return;
                            }
                        } else {
                            GameMessage gm = new GameMessage(TypeOfMove.MoveMotherNature, chosen);
                            String stringToSend = JsonConverter.fromMessageToJson(gm);

                            try {
                                response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                            } catch (IOException ex) {
                                System.out.println("impossibile to read the message: disconnected");
                                return;
                            }

                            if (response.getMessageType() == TypeOfMessage.Error) {
                                valid = false;
                                ErrorMessage responseError = (ErrorMessage) response;
                                if (responseError.getTypeOfError().equals(TypeOfError.InvalidChoice))
                                {
                                    System.out.println("steps not valid");
                                } else {
                                    System.out.println("wrong action");
                                }
                            } else {
                                gamestate = receiveUpdate();
                                gamestate = receiveUpdate();
                                moveMade = true;
                            }
                        }
                    } while (!valid || !moveMade);

                    // if() verificare che non sia finito il gioco

                    //CHOOSE CLOUD
                    do {
                        valid = true;
                        moveMade = false;
                        this.printRequest("choose cloud tile: ", gamestate);
                        chosen = VIEWclientCLI.readInt();
                        // the player uses character card
                        if (chosen >= 11 && chosen <= 13) {
                            try {
                                valid = useCharacterCard(chosen);
                                if (valid) {
                                    //gestire varie carte personaggio... meglio in altro metodo
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                return;
                            }
                        } else {

                            GameMessage gm = new GameMessage(TypeOfMove.IslandChoice, chosen);
                            String stringToSend = JsonConverter.fromMessageToJson(gm);
                            try {
                                response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
                            } catch (IOException ex) {
                                System.out.println("impossibile to read the message: disconnected");
                                return;
                            }
                            if (response.getMessageType() == TypeOfMessage.Error) {
                                valid = false;
                                ErrorMessage responseError = (ErrorMessage) response;
                                if (responseError.getTypeOfError().equals(TypeOfError.InvalidChoice))
                                    System.out.println("empty cloud");
                            } else {
                                gamestate = receiveUpdate();
                                moveMade = true;
                            }
                        }

                    } while (!valid || !moveMade);
                } // end of my turn action phase

            } while (gamestate.getCurrentPhase() == Phase.ACTION);

            gamestate = receiveUpdate();

        } while (!gamestate.isGameOver());

    }

    private void printRequest(String toPrint, GameStatePojo gameState) {
        if (gameState.isExpert()) {
            System.out.println(toPrint + " (11 / 12 / 13 for character cards)");
            return;
        }
        System.out.println(toPrint);
    }

    private void printStudentRequest(GameStatePojo gameState) {
        String toPrint = "move ";
        toPrint = toPrint + (gameState.getPlayers().size() == 2 ? "3" : "4");
        toPrint += "students";
        if (gameState.isExpert()) {
            System.out.println(toPrint + " (11 / 12 / 13 for character cards)");
            return;
        }
    }

    /**
     * show update of the game and handles possible errors: notMyTurn with recursion
     */
    private GameStatePojo receiveUpdate() {
        Message updateMessage;
        GameStatePojo gamestate;

        try {
            updateMessage = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
            //if (updateMessage.getMessageType().equals(TypeOfMessage.ACK)){System.out.println("E'arrivato un ack"); return null;}
            // debug
            System.out.println(updateMessage.getMessageType().toString());
        } catch (IOException ex) {
            System.out.println("impossibile to read the message: disconnected");
            return null;
        }
        if (updateMessage.getMessageType() == TypeOfMessage.Error){
            ErrorMessage errorMessage = (ErrorMessage) updateMessage;
            if (errorMessage.getTypeOfError().equals(TypeOfError.TurnError)){
                System.out.println("not your turn !");
            }
            return this.receiveUpdate();
        } else if (updateMessage.getMessageType() == TypeOfMessage.Update) {
            gamestate = ((UpdateMessage) updateMessage).getGameState();
            VIEWclientCLI.show(gamestate);
            return gamestate;
        }
        else if (updateMessage.getMessageType() == TypeOfMessage.Async){
            System.out.println("DisconnectionMessage !");
            return null;
        }
        return null;
    }

    private boolean useCharacterCard(int chosen) throws IOException {
        boolean valid = true;
        GameStatePojo gamestate;
        UpdateMessage updateMessage;
        GameMessage gm = new GameMessage(TypeOfMove.CharacterCard, chosen - 10);
        String stringToSend = JsonConverter.fromMessageToJson(gm);
        try {
            lineClient.getOut().write(stringToSend);
            lineClient.getOut().flush();
        } catch (IOException e) {
            System.out.println("impossible to send the message: disconnected");
            lineClient.endClient();
            throw new IOException();
        }
        Message response = JsonConverter.fromJsonToMessage(lineClient.readFromBuffer());
        if (response.getMessageType() == TypeOfMessage.Error) {
            ErrorMessage errorMessage = (ErrorMessage) response;
            valid = false;
            if (errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice)) {
                System.out.println("index invalid");
            } else if (errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice)) {
                System.out.println("there is an other effect activated");
            } else if (errorMessage.getTypeOfError().equals(TypeOfError.NoMoney)) {
                System.out.println("you don't have enought money");
            } else
                System.out.println("wrong action");
        } else {
            gamestate = receiveUpdate();
        }
        return valid;
    }
}
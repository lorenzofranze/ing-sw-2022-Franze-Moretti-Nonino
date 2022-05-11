package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;

import java.util.Scanner;

public class CLIView implements View {
    Scanner scanner = new Scanner(System.in);
    ClientController clientController = ClientController.getInstance();
    JsonConverter jsonConverter = new JsonConverter();

    @Override
    public void chooseGameMode() {

        System.out.println("\n\n");
        System.out.println("\033[01m" + "-------------------------------------------------ERYANTIS LOGIN-------------------------------------------------" + "\033[0m");


        System.out.println("\nTHESE ARE THE POSSIBLE GAME MODES:");
        System.out.println("1. 2 players simple\n" + "2. 3 players simple\n" + "3. 2 players complex\n" + "4. 3 players complex");
        System.out.print("\nCHOOSE THE GAME MODE: ");
        boolean valid;
        int result = 0;
        do {
            valid = true;
            String resultString = scanner.nextLine();
            if (!(resultString.equals("1") || resultString.equals("2") || resultString.equals("3") || resultString.equals("4"))){
                System.out.println("You must insert a number between 1 and 4.");
                System.out.print("CHOOSE THE GAME MODE: ");
                valid = false;
            }else{
                result = Integer.parseInt(resultString);
                if (result <= 0 || result >= 5) {
                    System.out.println("You must insert a number between 1 and 4.");
                    System.out.print("CHOOSE THE GAME MODE: ");
                    valid = false;
                }
            }
        } while (!valid);
        clientController.setGameMode(GameMode.values()[result-1]);
    }

    @Override
    public void beginReadUsername() {
        System.out.print("INSERT NICKNAME (at least 4 characters): ");
        boolean valid;
        String result;
        do {
            valid = true;
            result = scanner.nextLine();
            if (result.length() < 4 ) {
                System.out.println("You must insert a nickname of at least 4 characters.");
                System.out.print("INSERT NICKNAME (at least 4 characters): ");
                valid = false;
            }
        } while (!valid);
        clientController.setNickname(result);
    }

    @Override
    public void chooseAssistantCard() {
        String resultString;
        int result = 0;
        boolean valid = true;
        System.out.println("\033[01m"+"PIANIFICATION PHASE"+"\033[0m" + "\nYou need to choose your Assistant Card. Here is your deck.\n");

        PlayerPojo me = null;
        for (PlayerPojo p : clientController.getGameStatePojo().getPlayers()){
            if (p.getNickname().equals(ClientController.getInstance().getNickname())){
                me = p;
            }
        }

        for(AssistantCardPojo a : me.getDeck()){
            System.out.println("AssistantCard: " + a.toString());
        }

        System.out.print("\n");
        do{
            valid = true;
            System.out.print("Insert the turnOrder value of the card you want to play: ");
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
            } catch(NumberFormatException e){
                System.out.print("Insert the turnOrder value of the card you want to play: ");
                valid = false;
                resultString = scanner.nextLine();
            }
        }while(valid == false);

        clientController.getConsole().setAssistantCardPlayed(result);
    }

    @Override
    public void showMessage(Message message) {
        if(message.getMessageType().equals(TypeOfMessage.Connection)){
            ConnectionMessage connectionMessage = (ConnectionMessage) message;
            showConnection(connectionMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Ack)){
            AckMessage ackMessage = (AckMessage) message;
            showAck(ackMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Update)){
            UpdateMessage updateMessage = (UpdateMessage) message;
            showUpdate(updateMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Game)){
            GameMessage gameMessage = (GameMessage) message;
            showMove(gameMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Error)){
            ErrorMessage errorMessage = (ErrorMessage) message;
            showError(errorMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Ping)){
            PingMessage pingMessage = (PingMessage) message;
            showPing(pingMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Async)){
            AsyncMessage asyncMessage = (AsyncMessage) message;
            showAsync(asyncMessage);
        }

    }

    @Override
    public void showError(ErrorMessage errorMessage) {
        switch(errorMessage.getTypeOfError()) {
            case UsedName:
                System.out.println("Nickname already in use by other players.");
                break;
            case Disconnection:
                System.out.println("Disconnection error.");
                break;
            case UnmatchedMessages:
                System.out.println("Unexpected message received from server.");
                break;
            case FullDiningRoom:
                System.out.println("The diningroom is full. Place the student in another place.");
                break;
            case InvalidChoice:
                System.out.println("The choice you made is invalid.");
                break;
            case TurnError:
                System.out.println("You cannot play. Wait for your turn.");
                break;
            case NoMoney:
                System.out.println("You don't have enough money.");
                break;
            case FailedConnection:
                System.out.println("Failed connection to the server.");
                break;
            default:
                System.out.println("Unknown error message");
        }
    }

    @Override
    public void showConnection(ConnectionMessage connectionMessage) {
        String stringConnection = jsonConverter.fromMessageToJson(connectionMessage);
        System.out.println(connectionMessage);
    }

    @Override
    public void showAck(AckMessage ackMessage) {

        switch(ackMessage.getTypeOfAck()) {
            case CorrectConnection:
                System.out.println("Successful connection.");
                break;
            case CompleteLobby:
                System.out.println("All players have joined the lobby. The game can start.");
                System.out.println("\n\n\n##########################################     WELCOME IN ERYANTIS!     ##########################################");
                break;
            default:
                System.out.println("Unknown ack message");
        }
    }

    @Override
    public void showUpdate(UpdateMessage updateMessage) {
        GameStatePojo gameStatePojo = updateMessage.getGameState();
        showGameState(gameStatePojo);
    }

    @Override
    public void showAsync(AsyncMessage asyncMessage) {
        String stringAsync = jsonConverter.fromMessageToJson(asyncMessage);
        System.out.println(stringAsync);
    }

    @Override
    public void showMove(GameMessage gameMessage) {
        String stringMove = jsonConverter.fromMessageToJson(gameMessage);
        System.out.println(stringMove);
    }

    @Override
    public void showPing(PingMessage pingMessage) {
        String stringPing = jsonConverter.fromMessageToJson(pingMessage);
        System.out.println(stringPing);
    }

    @Override
    public void showGameState(GameStatePojo gameStatePojo) {

        System.out.println("\n----------------------------------------------------GAME STATE----------------------------------------------------\n");

        System.out.println("\033[01m" + "ISLANDS" + "\033[0m");
        int i=0;
        for(IslandPojo islandPojo : gameStatePojo.getIslands()){
            System.out.println("ISLAND "+i+":"+ "\ttowers: "+ islandPojo.getTowerCount()+" " +
                    (islandPojo.getTowerColour()!=null ? islandPojo.getTowerColour().toString() : " ")
                    + "\tstudents: " + islandPojo.getStudents() +
                    (islandPojo.getNumNoEntryTile()>0 ? "\tno entry tile(s):" + islandPojo.getNumNoEntryTile() : "")+
                    (islandPojo.isHasMotherNature() ? "\tMotherNature" : ""));
            i++;
        }


        System.out.println("\n\033[01m" + "CLOUDS" + "\033[0m");
        for(CloudPojo cloudPojo : gameStatePojo.getClouds())
            System.out.println("cloud "+ cloudPojo.getCloudId()+ ": " + cloudPojo.getStudents());


        for(PlayerPojo playerPojo : gameStatePojo.getPlayers()){
            System.out.println("\n\033[01m" + playerPojo.getNickname().toUpperCase() + " SCHOOLBOARD" + "\033[0m");
            System.out.println("ENTRANCE: " + playerPojo.getSchoolBoard().getEntrance());
            System.out.println("DINING ROOM: "+ playerPojo.getSchoolBoard().getDiningRoom());
            System.out.println("PROFESSORS: " + playerPojo.getSchoolBoard().getProfessors());
            System.out.println("TOWERS OWNED: " + playerPojo.getSchoolBoard().getSpareTowers());
        }

        System.out.println("\n\033[01m" + "ASSISTANT CARD PLAYED" + "\033[0m");
        for(PlayerPojo playerPojo : gameStatePojo.getPlayers()) {
            if(playerPojo.getPlayedAssistantCard()!=null)
                System.out.println(playerPojo.getNickname() + ": TurnOrder -> " + playerPojo.getPlayedAssistantCard().getTurnOrder() + " \t MovementsMotherNature -> " + playerPojo.getPlayedAssistantCard().getMovementsMotherNature());
            else
                System.out.println(playerPojo.getNickname() +": no assistant card played");
        }


        if(gameStatePojo.isExpert()){
            i=0;
            for(CharacterPojo characterPojo : gameStatePojo.getCharacters())
                System.out.println("character card "+i+": cost: "+ characterPojo.getActualCost());
            //System.out.println() ogg. sulla carta
        }

        System.out.print("\n\033[01m" + "TURN: " + "\033[0m");
        PlayerPojo current = gameStatePojo.getCurrentPlayer();
        if (current == null){
            System.out.println("no one");
        }else{
            if (current.getNickname().equals(clientController.getNickname())){
                System.out.println("yours");
            }else{
                System.out.println(current.getNickname());
            }
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------\n");
    }
}

package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.Controller.CharacterCardsConsole;
import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.Console;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;

import java.util.List;
import java.util.Scanner;

public class CLIView implements View {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public synchronized void chooseGameMode() {
        ClientController clientController = ClientController.getInstance();

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
    public synchronized void beginReadUsername() {
        ClientController clientController = ClientController.getInstance();
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
    public synchronized void chooseAssistantCard() {
        ClientController clientController = ClientController.getInstance();
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
        System.out.print("Insert the turnOrder value of the card you want to play: ");
        do{
            valid = true;
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
            } catch(NumberFormatException e){
                System.out.print("Insert the turnOrder value of the card you want to play: ");
                valid = false;
            }
        }while(valid == false);

        clientController.getConsole().setAssistantCardPlayed(result);
    }

    @Override
    public synchronized void askForCharacter() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        Console console = clientController.getConsole();
        List<? extends CharacterPojo> characterPojoList = gameStatePojo.getCharacters();
        String resultString;
        Integer result = null;

        System.out.println("\n"+"\033[01m"+"CHARACTER MENU"+"\033[0m");
        for (CharacterPojo c : characterPojoList){
            System.out.println(c.toString());
        }
        boolean valid = false;
        System.out.print("\nDo you want to play a Character Card (y/n)? ");
        do {
            resultString = scanner.nextLine();
            if (resultString.equals("y") || resultString.equals("n")){
                valid = true;
            }else{
                System.out.println("Invalid choice.");
                System.out.print("\nDo you want to play a Character Card (y/n)? ");
            }
        }while(valid == false);

        if (resultString.equals("n")){
            console.setCharacterPlayed(null);
            return;
        }

        valid = false;
        System.out.print("Insert the Character Card you want to play: ");
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.print("Invalid choice.");
                System.out.println("Insert the number of the Character Card you want to play: ");
            }
        }while(valid == false);

        console.setCharacterPlayed(result);
    }

    @Override
    public synchronized void moveStudent() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        Console console = clientController.getConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        System.out.println("\n"+"\033[01m"+"MOVE A STUDENT FROM YOUR ENTRANCE\n"+"\033[0m");
        System.out.println("These are the colours you can choose from");
        for (ColourPawn c : ColourPawn.values()){
            System.out.println(c.getIndexColour() + " - " + c.toString() + ".");
        }
        System.out.print("\nChoose a colour (insert the numerical index): ");

        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice.");
                System.out.print("\nChoose a colour (insert the numerical index): ");
                valid = false;
            }
        }while(valid == false);

        console.setPawnColour(result);

        System.out.println("\nYou can place the student on an island or on your DiningRoom.");
        System.out.println("\t- Insert the island index if you want to place it on an island");
        System.out.println("\t- Insert '-1' if you want to place it in your DiningRoom");

        System.out.print("\nInsert your choice: ");
        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice.");
                System.out.print("\nInsert your choice (numerical index): ");
                valid = false;
            }
        }while(valid == false);

        console.setPawnWhere(result);
    }

    @Override
    public synchronized void placeMotherNature() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        Console console = clientController.getConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        int i = gameStatePojo.getCurrentPlayer().getPlayedAssistantCard().getMovementsMotherNature();
        System.out.println("\n"+"\033[01m"+"MOVE MOTHERNATURE: watch out! Your steps are limited to " + i + ".\n"+"\033[0m");
        System.out.print("Insert the number of steps you want to make: ");

        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice. You must choose a number.");
                System.out.print("Insert the number of steps you want to make: ");
            }
        }while(valid == false);

        console.setStepsMotherNature(result);
    }

    @Override
    public synchronized void chooseCloud() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        Console console = clientController.getConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        System.out.println("\n"+"\033[01m"+"CHOOSE YOUR CLOUD"+"\033[0m");
        System.out.print("Insert cloud index: ");

        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice. You must choose a number.");
                System.out.print("Insert cloud index: ");
            }
        }while(valid == false);
        console.setCloudChosen(result);
    }

    @Override
    public synchronized void showMessage(Message message) {
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
    public synchronized void showError(ErrorMessage errorMessage) {
        switch(errorMessage.getTypeOfError()) {
            case UsedName:
                System.out.println("Nickname already in use by other players.\n");
                break;
            case UnmatchedMessages:
                System.out.println("Unexpected message received from server.\n");
                break;
            case FullDiningRoom:
                System.out.println("The diningroom is full. Place the student in another place.\n");
                break;
            case InvalidChoice:
                System.out.println("The choice you made is invalid.\n");
                break;
            case AlreadyPlayed:
                System.out.println("Another player has already played this card.\n");
                break;
            case TurnError:
                System.out.println("You cannot play. Wait for your turn.\n");
                break;
            case NoMoney:
                System.out.println("You don't have enough money.\n");
                break;
            case FailedConnection:
                System.out.println("Failed connection to the server.\n");
                break;
            default:
                System.out.println("Unknown error message.\n");
        }
    }

    @Override
    public synchronized void showConnection(ConnectionMessage connectionMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringConnection = jsonConverter.fromMessageToJson(connectionMessage);
        System.out.println(connectionMessage);
    }

    @Override
    public synchronized void showAck(AckMessage ackMessage) {

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
                break;
        }
    }

    @Override
    public synchronized void showUpdate(UpdateMessage updateMessage) {
        GameStatePojo gameStatePojo = updateMessage.getGameState();
        showGameState(gameStatePojo);
    }

    @Override
    public synchronized void showAsync(AsyncMessage asyncMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringAsync = jsonConverter.fromMessageToJson(asyncMessage);
        System.out.println(stringAsync);
    }

    @Override
    public synchronized void showMove(GameMessage gameMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringMove = jsonConverter.fromMessageToJson(gameMessage);
        System.out.println(stringMove);
    }

    @Override
    public synchronized void showPing(PingMessage pingMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringPing = jsonConverter.fromMessageToJson(pingMessage);
        System.out.println(stringPing);
    }

    @Override
    public synchronized void showGameState(GameStatePojo gameStatePojo) {
        ClientController clientController = ClientController.getInstance();

        System.out.println("\n----------------------------------------------------GAME STATE----------------------------------------------------\n");

        System.out.println("\033[01m" + "ISLANDS" + "\033[0m");
        int i=0;
        for(IslandPojo islandPojo : gameStatePojo.getIslands()){
            System.out.println("ISLAND "+i+":"
                    + "\tstudents: " + islandPojo.getStudents() + "\ttowers: "+ islandPojo.getTowerCount()+" - " +
                    (islandPojo.getTowerColour()!=null ? islandPojo.getTowerColour().toString() : " ") +
                    (islandPojo.getNumNoEntryTile()>0 ? "\tno entry tile(s):" + islandPojo.getNumNoEntryTile() : "")+
                    (islandPojo.isHasMotherNature() ? "\tMotherNature" : ""));
            i++;
        }


        System.out.println("\n"+"\033[01m" + "CLOUDS" + "\033[0m");
        for(CloudPojo cloudPojo : gameStatePojo.getClouds())
            System.out.println("cloud "+ cloudPojo.getCloudId()+ ": " + cloudPojo.getStudents());


        for(PlayerPojo playerPojo : gameStatePojo.getPlayers()){
            System.out.println("\n\033[01m" + playerPojo.getNickname().toUpperCase() + " SCHOOLBOARD" + "\033[0m");
            System.out.println("ENTRANCE: " + playerPojo.getSchoolBoard().getEntrance());
            System.out.println("DINING ROOM: "+ playerPojo.getSchoolBoard().getDiningRoom());
            System.out.println("PROFESSORS: " + playerPojo.getSchoolBoard().getProfessors());
            System.out.println("TOWERS OWNED: " + playerPojo.getSchoolBoard().getSpareTowers());
            if (gameStatePojo.isExpert()){
                System.out.println("MONEY: " + playerPojo.getCoins());
            }
        }

        System.out.println("\n"+"\033[01m" + "ASSISTANT CARD PLAYED" + "\033[0m");
        for(PlayerPojo playerPojo : gameStatePojo.getPlayers()) {
            if(playerPojo.getPlayedAssistantCard()!=null)
                System.out.println(playerPojo.getNickname() + ": TurnOrder -> " + playerPojo.getPlayedAssistantCard().getTurnOrder() + " \t MovementsMotherNature -> " + playerPojo.getPlayedAssistantCard().getMovementsMotherNature());
            else
                System.out.println(playerPojo.getNickname() +": no assistant card played");
        }


        if(gameStatePojo.isExpert()){
            System.out.println("\n"+"\033[01m"+"CHARACTER MENU"+"\033[0m");
            for (CharacterPojo c : gameStatePojo.getCharacters()){
                System.out.println(c.toString());
            }
            //System.out.println() ogg. sulla carta
        }

        System.out.print("\n"+"\033[01m" + "TURN: " + "\033[0m");
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

        //game ended with winner or draw
        if(gameStatePojo.isGameOver() && gameStatePojo.getWinner()!=null) {
            if(gameStatePojo.getWinner().equals("?"))
                System.out.println("\u001B[41m"+"GAME ENDED. WINNER: PAREGGIO"+ "\u001B[0m");
            else{
                System.out.println("\u001B[41m"+"GAME ENDED. WINNER: " +gameStatePojo.getWinner()+ "\u001B[0m");

            }
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------\n");
    }

    //METHODS FOR COMPLEX MODE:
    @Override
    public synchronized void moveStudentToIsland(){
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        CharacterCardsConsole characterCardsConsole = clientController.getCharacterCardsConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        System.out.println("\n"+"\033[01m"+"CARD1 EFFECT\n"+"\033[0m");
        System.out.println("These are the colours you can choose from");
        for (ColourPawn c : ColourPawn.values()){
            System.out.println(c.getIndexColour() + " - " + c.toString() + ".");
        }
        System.out.print("\nChoose a colour (insert the numerical index): ");

        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice.");
                System.out.print("\nChoose a colour (insert the numerical index): ");
                valid = false;
            }
        }while(valid == false);

        characterCardsConsole.setPawnColour(result);

        System.out.println("\n - Insert the island index to place it on an island");
        System.out.print("\nInsert your choice: ");

        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice.");
                System.out.print("\nInsert your choice (numerical index): ");
                valid = false;
            }
        }while(valid == false);
        characterCardsConsole.setPawnWhere(result);
    }

    @Override
    public synchronized void chooseIsland(){
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        CharacterCardsConsole characterCardsConsole = clientController.getCharacterCardsConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        int cardID = gameStatePojo.getActiveEffect().getCharacterId(); // can be 3 or 5

        System.out.println("\n"+"\033[01m"+"CARD"+cardID+" EFFECT"+"\033[0m");
        System.out.print("Insert island index: ");

        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice. You must choose a number.");
                System.out.print("Insert island index: ");
                valid = false;
            }
        }while(valid == false);
        characterCardsConsole.setPawnWhere(result);
    }

    @Override
    public synchronized void chooseColour() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        CharacterCardsConsole characterCardsConsole = clientController.getCharacterCardsConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        int cardID = gameStatePojo.getActiveEffect().getCharacterId();
        if(cardID!=7 && cardID!=10) {
            System.out.println("\n" + "\033[01m" + "CARD" + cardID + " EFFECT" + "\033[0m");
        }
        System.out.println("These are the colours you can choose from");
        for (ColourPawn c : ColourPawn.values()){
            System.out.println(c.getIndexColour() + " - " + c.toString() + ".");
        }
        System.out.print("Insert colour: ");
        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice. You must choose a number.");
                System.out.print("Insert colour: ");
            }
        }while(valid == false);
        characterCardsConsole.setPawnColour(result);
    }

    /** this method is used by card 7 and 10 to make the player choose the number of pawns he wants to move */
    @Override
    public synchronized void chooseNumOfMove() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        CharacterCardsConsole characterCardsConsole = clientController.getCharacterCardsConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        int cardID = gameStatePojo.getActiveEffect().getCharacterId(); // can be 7 or 10

        System.out.println("\n"+"\033[01m"+"CARD"+cardID+" EFFECT"+"\033[0m");
        System.out.print("Insert the number of students you want to move: ");

        valid = false;
        do {
            resultString = scanner.nextLine();
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice. You must choose a number.");
                System.out.print("Insert number of students you want to move: ");
            }
        }while(valid == false);
        characterCardsConsole.setPawnsToMove(result);
        if(cardID==7){
            System.out.println("\nFirst choose the student on the card then the student in your entrance");
        }else if(cardID==10){
            System.out.println("\nFirst choose the student in your entrance then the student in your dining room");
        }
    }


}

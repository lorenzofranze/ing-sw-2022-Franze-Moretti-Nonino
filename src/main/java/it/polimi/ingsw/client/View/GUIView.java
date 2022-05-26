package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;

import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static javafx.application.Application.launch;


public class GUIView extends Application implements View{

    private Stage currentStage;
    private Parent root;

    @Override
    /**
     * Method that initialize stage and load scenes
     * @param primaryStage game stage
     * @throws Exception impossible start game
     */
    public void start(Stage primaryStage) throws Exception {
        try {
            this.root = FXMLLoader.load(getClass().getClassLoader().getResource("startFrame.fxml"));
            Scene scene = new Scene(root);
            //primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showScene(String nameFileFxml) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(nameFileFxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentStage.setScene(new Scene(root, 800, 500));
        currentStage.show();
    }

    void beginUsername(MouseEvent event){
        //Stage startWindow= (Stage) tfTitle.getScene().getWindow();
        //String title=
    }

    @Override
    public void startScreen(){
        showScene("startFrame.fxml");
    }


    @Override
    public synchronized void chooseGameMode() {
        ClientController clientController = ClientController.getInstance();
        showScene("chooseGameModeFrame.fxml");

        System.out.println("\nTHESE ARE THE POSSIBLE GAME MODES:");
        System.out.println("1. 2 players simple\n" + "2. 3 players simple\n" + "3. 2 players complex\n" + "4. 3 players complex");
        System.out.print("\nCHOOSE THE GAME MODE: ");

    }

    @FXML
    public void inputChoice1(MouseEvent event) {
        ClientController clientController = ClientController.getInstance();
        clientController.setGameMode(GameMode.values()[0]);
    }

    @FXML
    public void inputChoice2(MouseEvent event) {
        ClientController clientController = ClientController.getInstance();
        clientController.setGameMode(GameMode.values()[1]);
    }

    @FXML
    public void inputChoice3(MouseEvent event) {
        ClientController clientController = ClientController.getInstance();
        clientController.setGameMode(GameMode.values()[2]);
    }

    @FXML
    public void inputChoice4(MouseEvent event) {
        ClientController clientController = ClientController.getInstance();
        clientController.setGameMode(GameMode.values()[3]);
    }

    public void beginReadUsername() {
            ClientController clientController = ClientController.getInstance();
            System.out.print("INSERT NICKNAME (at least 4 characters): ");
            boolean valid;
            String result;
            /*
            do {
                valid = true;
                result = scanner.nextLine();
                if (result.length() < 4 ) {
                    System.out.println("You must insert a nickname of at least 4 characters.");
                    System.out.print("INSERT NICKNAME (at least 4 characters): ");
                    valid = false;
                }
             */
            //} while (!valid);
            //clientController.setNickname(result);
        }



    @Override
    public void chooseAssistantCard() {

    }

    @Override
    public void askForCharacter() {

    }

    @Override
    public void moveStudent() {

    }

    @Override
    public void placeMotherNature() {

    }

    @Override
    public void chooseCloud() {

    }

    @Override
    public void showMessage(Message message) {

    }

    @Override
    public void showConnection(ConnectionMessage connectionMessage) {

    }

    @Override
    public void showError(ErrorMessage errorMessage) {

    }

    @Override
    public void showAck(AckMessage ackMessage) {

    }

    @Override
    public void showUpdate(UpdateMessage updateMessage) {

    }

    @Override
    public void showAsync(AsyncMessage asyncMessage) {

    }

    @Override
    public void showPing(PingMessage pingMessage) {

    }

    @Override
    public void showMove(GameMessage gameMessage) {

    }

    @Override
    public void showGameState(GameStatePojo gameStatePojo) {

    }

    @Override
    public void moveStudentToIsland() {

    }

    @Override
    public void chooseColour() {

    }

    @Override
    public void chooseIsland() {

    }

    @Override
    public void chooseNumOfMove() {

    }



}

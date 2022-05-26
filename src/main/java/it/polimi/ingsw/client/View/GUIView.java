package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;

import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.ActionEvent;
import java.io.IOException;

import static javafx.application.Application.launch;


public class GUIView extends Application implements View{

    private Stage currentStage;
    private Parent root;

    private int gameModeChosen;

    /**
     * Method that initialize stage and load scenes
     * it calls chooseGameModeGUI on muoseClicked
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

    public void setCurrentStage(String fxmlName){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.show();
    }

    /**
     * it shows the game mode and it calls input Choice1/2/3/4 on mouse clicked
     */
    public void chooseGameModeGUI(MouseEvent mouseEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("chooseGameModeFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.show();
    }


    /**
     * when the client controller calls chooseGameMode, the client controller's attribute "gameMode" is
     * set with the value gameModeChosen
     */
    @Override
    public synchronized void chooseGameMode(){

        ClientController clientController = ClientController.getInstance();
        clientController.setGameMode(GameMode.values()[gameModeChosen]);
    }



    @FXML
    public void inputChoice1(MouseEvent event) {
        this.gameModeChosen=0;
        setCurrentStage("chooseNameFrame.fxml");
    }

    @FXML
    public void inputChoice2(MouseEvent event) {
        this.gameModeChosen=1;
        setCurrentStage("chooseNameFrame.fxml");
    }

    @FXML
    public void inputChoice3(MouseEvent event) {
        this.gameModeChosen=2;
        setCurrentStage("chooseNameFrame.fxml");
    }

    @FXML
    public void inputChoice4(MouseEvent event) {
        this.gameModeChosen=3;
        setCurrentStage("chooseNameFrame.fxml");
    }

    void beginUsername(MouseEvent event){
        //Stage startWindow= (Stage) tfTitle.getScene().getWindow();
        //String title=
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

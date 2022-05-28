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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.ActionEvent;
import java.io.IOException;

import static javafx.application.Application.launch;


public class GUIView extends Application implements View {

    private static Stage currentStage;
    private static Parent root;

    private int gameModeChosen;
    private String nameChosen;

    /**
     * Method that initialize stage and load scenes
     * it calls chooseGameModeGUI on muoseClicked
     *
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentStage(String fxmlName) {
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
    public synchronized void chooseGameMode() {

        ClientController clientController = ClientController.getInstance();
        clientController.setGameMode(GameMode.values()[gameModeChosen]);
        System.out.println("game mode chosen: " + gameModeChosen);
    }


    @FXML
    public void inputChoice1(MouseEvent mouseEvent) {
        this.gameModeChosen = 0;
        currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        setCurrentStage("chooseNameFrame.fxml");
    }

    @FXML
    public void inputChoice2(MouseEvent mouseEvent) {
        this.gameModeChosen = 1;
        currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        setCurrentStage("chooseNameFrame.fxml");
    }

    @FXML
    public void inputChoice3(MouseEvent mouseEvent) {
        this.gameModeChosen = 2;
        currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        setCurrentStage("chooseNameFrame.fxml");
    }

    @FXML
    public void inputChoice4(MouseEvent mouseEvent) {
        this.gameModeChosen = 3;
        currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        setCurrentStage("chooseNameFrame.fxml");
    }

    @FXML
    private TextField TextFieldNickname;

    @FXML
    private Label LabelNickname;

    @FXML
    public void nameCheck(MouseEvent mouseEvent){
        boolean valid;
        String result;

        valid = true;
        result= TextFieldNickname.getText();
        System.out.println("controllo nome");
        if (result.length() < 4) {
            valid = false;
            LabelNickname.setText("The nickname is too short, choose another one");
            System.out.println("nome sbagliato");

            result= TextFieldNickname.getText();
            return;
        }
        else{
            currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            setCurrentStage("gameTableFrame.fxml");
        }

    }


    public void beginReadUsername() {
        ClientController clientController = ClientController.getInstance();
        clientController.setNickname(nameChosen);
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

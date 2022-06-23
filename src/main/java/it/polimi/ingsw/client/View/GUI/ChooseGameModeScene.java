package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**this class contains the GUI methods that are fired when the user select the game mode */
public class ChooseGameModeScene {

    @FXML
    public void inputChoice1(MouseEvent mouseEvent) {
        ClientController.getInstance().setGameMode(GameMode.values()[0]);
        ClientController.getSemaphore().release();
        GuiController.getInstance().switchNameScene();
    }

    @FXML
    public void inputChoice2(MouseEvent mouseEvent) {
        ClientController.getInstance().setGameMode(GameMode.values()[2]);
        ClientController.getSemaphore().release();
        GuiController.getInstance().switchNameScene();
    }

    @FXML
    public void inputChoice3(MouseEvent mouseEvent) {
        ClientController.getInstance().setGameMode(GameMode.values()[1]);
        ClientController.getSemaphore().release();
        GuiController.getInstance().switchNameScene();
    }

    @FXML
    public void inputChoice4(MouseEvent mouseEvent) {
        ClientController.getInstance().setGameMode(GameMode.values()[3]);
        ClientController.getSemaphore().release();
        GuiController.getInstance().switchNameScene();
    }
}

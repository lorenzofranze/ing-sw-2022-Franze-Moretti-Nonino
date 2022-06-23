package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**this class contains the GUI methods that are fired when the user has chosen his nickname */
public class ChooseNameScene {

    public void inizialize(){
        okButton.setDisable(true);
    }

    @FXML
    private Button okButton;

    @FXML
    private javafx.scene.control.TextField TextFieldNickname;

    @FXML
    private Label LabelNickname;

    /**
     * check the entered name: it must be longer than 4
     * @param mouseEvent
     */
    @FXML
    public void nameCheck(ActionEvent mouseEvent){
        String result;

        result= TextFieldNickname.getText();
        if (result == null || result.length() < 4) {
            LabelNickname.setText("The nickname is too short, choose another one");
            result= TextFieldNickname.getText();
        }
        else{
            //lenght ok but maybe not unique
            ClientController.getInstance().setNickname(result);
            ClientController.getSemaphore().release();
        }
    }

    /**
     * okButton is disabled by initialize(), and whenever I enter a character (i.e. a
     * keyEvent), lengthCheck is invoked. lengthCheck checks the length of the nickname entered, if this
     * is longer than 3 characters, it enables the okButton
     * @param event
     */
    @FXML
    void lengthCheck(KeyEvent event) {
        okButton.setDisable(TextFieldNickname.getText().length()<4);
    }


}

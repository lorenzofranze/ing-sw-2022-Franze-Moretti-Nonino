package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
     * controlla il nome inserito: se è più lungo di 4, cambia scena
     * senò lo richiede
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

    @FXML
    void lengthCheck(KeyEvent event) {
        okButton.setDisable(TextFieldNickname.getText().length()<4);
    }


}

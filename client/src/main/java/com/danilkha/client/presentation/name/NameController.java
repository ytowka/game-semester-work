package com.danilkha.client.presentation.name;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NameController {

    private NameModel model;

    @FXML
    Label errorLabel;

    @FXML
    TextField textField;

    @FXML
    Button joinButton;

    public void init(NameModel model){
        this.model = model;

        model.error.addObserver(error -> {
            errorLabel.setText(error);
        });
    }

    @FXML
    public void onJoinClicked(){
        model.onJoinClicked(textField.getText());
    }
}

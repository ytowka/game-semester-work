package com.danilkha.client.presentation.menu;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuController {


    @FXML
    Label welcomeText;

    private MenuModel model;
    void init(MenuModel menuModel){
        model = menuModel;

        model.text.addObserver(value -> {
            welcomeText.setText(value);
        });
    }

}

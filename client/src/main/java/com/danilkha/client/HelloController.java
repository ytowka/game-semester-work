package com.danilkha.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    public Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        clickListener.run();
    }

    private Runnable clickListener = null;
    public void setOnClickListener(Runnable clickListener){
        this.clickListener = clickListener;
    }
}
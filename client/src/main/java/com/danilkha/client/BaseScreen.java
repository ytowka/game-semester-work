package com.danilkha.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;

import java.io.IOException;

public abstract class BaseScreen<C> extends Scene {

    FXMLLoader loader;

    C controller;

    public BaseScreen(String resourcePath) {
        super(new Group(), 320,240);
        loader = new FXMLLoader(HelloApplication.class.getResource(resourcePath));
        controller = loader.getController();
        try {
            setRoot(loader.load());
        } catch (IOException e) {
            throw new RuntimeException("Screen root not found "+resourcePath);
        }
    }
}

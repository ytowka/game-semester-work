package com.danilkha.client.utils;

import com.danilkha.client.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;

import java.io.IOException;

public abstract class BaseScreen<C> {

    public final Scene scene;
    public final C controller;

    public BaseScreen(String resourcePath) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("presentation/"+resourcePath));
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Screen root not found "+resourcePath);
        }

        controller = loader.getController();

    }
}

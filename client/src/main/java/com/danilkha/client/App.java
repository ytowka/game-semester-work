package com.danilkha.client;

import com.danilkha.client.presentation.AppScreen;
import com.danilkha.client.presentation.menu.MenuModel;
import com.danilkha.client.utils.LiveData;
import javafx.application.Application;
import javafx.stage.Stage;
import org.danilkha.connection.SocketClientConnection;

import java.io.IOException;

public class App extends Application {

    private SocketClientConnection clientConnection;


    private LiveData<AppScreen> currentScreen = new LiveData<>(AppScreen.MainMenu);

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Hello!");

        currentScreen.addObserver(value ->{
            switch (value){
                case MainMenu -> stage.setScene(new MenuModel().scene);
                case NickName -> {
                }
                case Lobby -> {
                }
                case Game -> {
                }
            }
        });

        stage.show();
        /*Socket socket = new Socket(ServerConfig.HOST, ServerConfig.PORT);
        clientConnection = new SocketClientConnection(socket, (data -> {
            Platform.runLater(() ->{
                //helloController.welcomeText.setText(data);
            });
        }));

       *//* helloController.setOnClickListener(() ->{
            clientConnection.receiveData("getDate");
        });*//*

        clientConnection.start();*/
    }

    public static void main(String[] args) {
        launch();
    }
}
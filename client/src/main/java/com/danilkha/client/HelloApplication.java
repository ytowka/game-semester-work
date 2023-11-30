package com.danilkha.client;

import com.danilkha.client.menu.MenuScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.danilkha.config.ServerConfig;
import org.danilkha.connection.SocketClientConnection;
import org.danilkha.utils.observable.ObservableValue;

import java.io.IOException;
import java.net.Socket;

public class HelloApplication extends Application {

    private SocketClientConnection clientConnection;


    private ObservableValue<GameScreen> currentScreen = new ObservableValue<>(GameScreen.MainMenu);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        HelloController helloController = fxmlLoader.getController();
        stage.show();

        currentScreen.addObserver(new MainThreadObserver<>(value ->{
            switch (value){
                case MainMenu -> stage.setScene(new MenuScene());
                case NickName -> {
                }
                case Lobby -> {
                }
                case Game -> {
                }
            }
        }));
        Socket socket = new Socket(ServerConfig.HOST, ServerConfig.PORT);
        clientConnection = new SocketClientConnection(socket, (data -> {
            Platform.runLater(() ->{
                helloController.welcomeText.setText(data);
            });
        }));

        helloController.setOnClickListener(() ->{

            clientConnection.receiveData("getDate");
        });

        clientConnection.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
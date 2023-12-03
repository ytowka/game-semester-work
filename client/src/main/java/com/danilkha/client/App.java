package com.danilkha.client;

import com.danilkha.client.di.ServiceLocator;
import com.danilkha.client.presentation.AppScreen;
import com.danilkha.client.presentation.Navigator;
import com.danilkha.client.presentation.menu.MenuModel;
import com.danilkha.client.presentation.name.NameModel;
import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;
import javafx.application.Application;
import javafx.stage.Stage;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.LobbyDto;
import org.danilkha.utils.observable.EqualityPolicy;

public class App extends Application implements Navigator<AppScreen> {

    private LiveData<AppScreen> currentScreen = new LiveData<>(new AppScreen.Menu());
    ServiceLocator serviceLocator = ServiceLocator.getInstance();
    LobbyApi lobbyApi = serviceLocator.lobbyApi.get();

    NameModel.Callback nameCallback = name -> {

    };


    @Override
    public void start(Stage stage) {
        stage.setTitle("Tanchiki");


        currentScreen.addObserver(value ->{
            if(value != null){
                switch (value){
                    case AppScreen.Menu ignored -> {
                        stage.setScene(new MenuModel(lobbyApi, this).scene);
                    }
                    case AppScreen.NameInput nameInputScreen -> {
                        stage.setScene(new NameModel(nameInputScreen.lobby(), this).scene);
                    }
                    case AppScreen.LobbyRoom lobbyRoomScreen -> {
                        stage.setScene(new MenuModel(lobbyApi, this).scene);
                    }
                }
            }
        });

        stage.show();
        serviceLocator.socketClientConnection.get().start();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void navigate(AppScreen route) {
        currentScreen.setValue();
    }
}
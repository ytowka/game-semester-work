package com.danilkha.client;

import com.danilkha.client.di.ServiceLocator;
import com.danilkha.client.presentation.AppScreen;
import com.danilkha.client.presentation.menu.MenuModel;
import com.danilkha.client.presentation.name.NameModel;
import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;
import javafx.application.Application;
import javafx.stage.Stage;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.Lobby;
import org.danilkha.utils.observable.EqualityPolicy;

public class App extends Application {

    private LiveData<BaseScreen<?>> currentScreen = new LiveData<>(EqualityPolicy.REFERENTIAL);
    ServiceLocator serviceLocator = ServiceLocator.getInstance();
    LobbyApi lobbyApi = serviceLocator.lobbyApi.get();

    NameModel.Callback nameCallback = name -> {

    };

    MenuModel.Callback menuCallback = new MenuModel.Callback() {
        @Override
        public void onLobbySelected(Lobby lobby) {
            currentScreen.setValue(new NameModel(nameCallback, lobbyApi, lobby));
        }

        @Override
        public void onCreateNewLobbyClicked() {
            System.out.println("onCreateNewLobbyClicked");
            currentScreen.setValue(new NameModel(nameCallback, lobbyApi, null));
        }
    };

    @Override
    public void start(Stage stage) {
        stage.setTitle("Tanchiki");

        currentScreen.addObserver(value ->{

            if(value != null){
                System.out.println("change screem "+value.getClass().getName());
                stage.setScene(value.scene);
            }
        });

        currentScreen.setValue(new MenuModel(lobbyApi, menuCallback));

        stage.show();
        serviceLocator.socketClientConnection.get().start();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.danilkha.client;

import com.danilkha.client.di.ServiceLocator;
import com.danilkha.client.presentation.AppScreen;
import com.danilkha.client.presentation.Navigator;
import com.danilkha.client.presentation.game.GameModel;
import com.danilkha.client.presentation.lobby.LobbyModel;
import com.danilkha.client.presentation.menu.MenuModel;
import com.danilkha.client.presentation.name.NameModel;
import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;
import javafx.application.Application;
import javafx.stage.Stage;
import org.danilkha.api.GameRoundApi;
import org.danilkha.api.LobbyApi;
import org.danilkha.game.LobbyDto;
import org.danilkha.utils.observable.EqualityPolicy;

public class App extends Application implements Navigator<AppScreen> {

    private LiveData<AppScreen> currentScreen = new LiveData<>(new AppScreen.Menu());
    ServiceLocator serviceLocator = ServiceLocator.getInstance();
    LobbyApi lobbyApi = serviceLocator.lobbyApi.get();


    @Override
    public void start(Stage stage) {
        stage.setTitle("Tanchiki");

        currentScreen.addObserver(value ->{
            if(value != null){
                if (value instanceof AppScreen.Menu) {
                    stage.setScene(new MenuModel(lobbyApi, this).scene);
                } else if (value instanceof AppScreen.NameInput nameInputScreen) {
                    stage.setScene(new NameModel(lobbyApi, nameInputScreen.lobby(), this).scene);
                } else if (value instanceof AppScreen.LobbyRoom lobbyRoom) {
                    stage.setScene(new LobbyModel(lobbyApi, lobbyRoom.lobbyDto(), lobbyRoom.playerName(), this).scene);
                } else if(value instanceof AppScreen.Game game){
                    stage.setScene(new GameModel(
                            serviceLocator.gameApi.get(),
                            game.playerName(),
                            game.me()
                    ).scene);
                }
            }
        });
        stage.show();
        serviceLocator.socketClientConnection.get().start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        serviceLocator.socketClientConnection.get().stop();
        System.out.println("stop");
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void navigate(AppScreen route) {
        currentScreen.setValue(route);
    }
}
module com.danilkha.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires core;

    exports com.danilkha.client;
    exports com.danilkha.client.utils;
    exports com.danilkha.client.presentation;

    exports com.danilkha.client.presentation.menu;
    opens com.danilkha.client.presentation.menu to javafx.fxml;

    exports com.danilkha.client.presentation.name;
    opens com.danilkha.client.presentation.name to javafx.fxml;

    exports com.danilkha.client.presentation.lobby;
    opens com.danilkha.client.presentation.lobby to javafx.fxml;

    exports com.danilkha.client.presentation.game;
    opens com.danilkha.client.presentation.game to javafx.fxml;
}
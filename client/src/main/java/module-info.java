module com.danilkha.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires core;

    exports com.danilkha.client;
    exports com.danilkha.client.utils;
    exports com.danilkha.client.presentation.menu;
    opens com.danilkha.client.presentation.menu to javafx.fxml;
    exports com.danilkha.client.presentation;
}
module com.danilkha.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires core;


    opens com.danilkha.client to javafx.fxml;
    exports com.danilkha.client;
}
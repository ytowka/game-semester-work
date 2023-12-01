package com.danilkha.client.utils;

import javafx.application.Platform;
import org.danilkha.utils.observable.Observer;

public class MainThreadObserver<T> implements Observer<T> {

    private Observer<T> observer;
    public MainThreadObserver(Observer<T> observer){
        this.observer = observer;
    }
    @Override
    public void onChange(T value) {
        Platform.runLater(() -> observer.onChange(value));
    }
}

package com.danilkha.client.utils;

import javafx.application.Platform;
import org.danilkha.utils.observable.EqualityPolicy;
import org.danilkha.utils.observable.MutableObservableValue;

public class LiveData<T> extends MutableObservableValue<T> {

    public LiveData() {
        super();
    }

    public LiveData(EqualityPolicy equalityPolicy) {
        super(equalityPolicy);
    }

    public LiveData(T initialValue) {
        super(initialValue);
    }

    public LiveData(T initialValue, EqualityPolicy equalityPolicy) {
        super(initialValue, equalityPolicy);
    }

    public void postValue(T newValue){
        Platform.runLater(() -> {
            setValue(newValue);
        });
    }
}

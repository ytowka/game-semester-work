package org.danilkha.connection;

import org.danilkha.utils.observable.ObservableValue;
import org.danilkha.utils.observable.Observer;

public class DisposableObserver<T> {

    private final ObservableValue<T> observableValue;
    private final Observer<T> observer;

    public DisposableObserver(ObservableValue<T> observableValue, Observer<T> observer) {
        this.observableValue = observableValue;
        this.observer = observer;
        observableValue.addObserver(observer);
    }

    void dispose(){
        observableValue.removeObserver(observer);
    }
}

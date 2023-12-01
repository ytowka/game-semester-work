package org.danilkha.utils.observable;

import java.util.function.Function;

public interface ObservableValue<T> {
    T getValue();

    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void clearObservers();

    <O> ObservableValue<O> map(Function<? super T, ? extends O> mapper);
}

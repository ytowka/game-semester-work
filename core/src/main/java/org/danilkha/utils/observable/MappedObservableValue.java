package org.danilkha.utils.observable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MappedObservableValue<T, R> implements ObservableValue<R> {

    private final Map<Observer<R>, Observer<T>> newObservers;
    private final ObservableValue<T> original;
    private final Function<? super T, ? extends R> mapper;

    public MappedObservableValue( ObservableValue<T> original, Function<? super T, ? extends R> mapper) {
        this.newObservers = new HashMap<>();
        this.original = original;
        this.mapper = mapper;
    }


    @Override
    public R getValue() {
       return mapper.apply(original.getValue());
    }

    @Override
    public void addObserver(Observer<R> observer) {
        Observer<T> newObserver = value -> {
            observer.onChange(mapper.apply(value));
        };
        newObservers.put(observer, newObserver);
        original.addObserver(newObserver);
    }

    @Override
    public void removeObserver(Observer<R> observer) {
        Observer<T> originalObserver = newObservers.get(observer);
        original.removeObserver(originalObserver);
        newObservers.remove(observer);
    }

    @Override
    public void clearObservers() {
        original.clearObservers();
        newObservers.clear();
    }

    @Override
    public <O> ObservableValue<O> map(Function<? super R, ? extends O> mapper) {
        return new MappedObservableValue<R, O>(this, mapper);
    }

}

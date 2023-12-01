package org.danilkha.utils.observable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MutableObservableValue<T> implements ObservableValue<T> {
    private T value;

    public List<Observer<T>> observers = new ArrayList<>();

    private final EqualityPolicy equalityPolicy;

    public MutableObservableValue(){
        value = null;
        equalityPolicy = EqualityPolicy.STRUCTURAL;
    }

    public MutableObservableValue(EqualityPolicy equalityPolicy){
        value = null;
        this.equalityPolicy = equalityPolicy;
    }

    public MutableObservableValue(T initialValue){
        this.value = initialValue;
        equalityPolicy = EqualityPolicy.STRUCTURAL;
    }

    public MutableObservableValue(T initialValue, EqualityPolicy equalityPolicy){
        this.value = initialValue;
        this.equalityPolicy = equalityPolicy;
    }

    public synchronized void setValue(T newValue){
        boolean isChanged = switch (equalityPolicy){
            case REFERENTIAL -> value == newValue;
            case STRUCTURAL -> {
                if(value == null){
                    yield newValue != null;
                }else{
                    yield value.equals(newValue);
                }
            }
        };
        if(isChanged){
            value = newValue;
            observers.forEach(observer -> observer.onChange(newValue));
        }
    }

    @Override
    public T getValue(){
        return value;
    }

    @Override
    public void addObserver(Observer<T> observer){
        observer.onChange(value);
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void clearObservers(){
        observers.clear();
    }

    @Override
    public <O> ObservableValue<O> map(Function<? super T, ? extends O> mapper) {
        return new MappedObservableValue<>(this, mapper);
    }


}

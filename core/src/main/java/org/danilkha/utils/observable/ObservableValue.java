package org.danilkha.utils.observable;

import java.util.List;

public class ObservableValue<T> {
    private T value;

    public List<Observer<T>> observers;

    private final EqualityPolicy equalityPolicy;

    public ObservableValue(){
        value = null;
        equalityPolicy = EqualityPolicy.STRUCTURAL;
    }

    public ObservableValue(EqualityPolicy equalityPolicy){
        value = null;
        this.equalityPolicy = equalityPolicy;
    }

    public ObservableValue(T initialValue){
        this.value = initialValue;
        equalityPolicy = EqualityPolicy.STRUCTURAL;
    }

    public ObservableValue(T initialValue, EqualityPolicy equalityPolicy){
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

    public T getValue(){
        return value;
    }

    public void addObserver(Observer<T> observer){
        observer.onChange(value);
        observers.add(observer);
    }

    public void clearObservers(){
        observers.clear();
    }
}

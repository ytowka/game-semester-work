package org.danilkha.utils.observable;

import java.util.ArrayList;
import java.util.List;

public class Completable<T> {

    private T value;

    public List<Observer<T>> observers = new ArrayList<>();

    public Completable(){
        value = null;
    }

    public synchronized void complete(T newValue){
        value = newValue;
        observers.forEach(observer -> observer.onChange(newValue));
        observers.clear();
    }

    public void addObserver(Observer<T> observer){
        observer.onChange(value);
        observers.add(observer);
    }

    public void clearObservers(){
        observers.clear();
    }
}

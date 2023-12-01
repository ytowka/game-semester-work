package org.danilkha.utils.lazy;

public class Lazy<T> {

    private final Factory<T> factory;
    private T value = null;
    public Lazy(Factory<T> factory){
        this.factory = factory;
    }

    public synchronized T get(){
        if(value == null){
            value = factory.provide();
        }
        return value;
    }

    public interface Factory<T>{
        T provide();
    }
}


package org.danilkha.utils.observable;

import java.io.IOException;

public interface Observer<T> {
    void onChange(T value);
}

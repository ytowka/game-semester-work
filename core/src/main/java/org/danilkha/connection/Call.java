package org.danilkha.connection;

import org.danilkha.utils.observable.Completable;

public interface Call<T> {

    void onSuccess(T data);

    void onError(Throwable t);
}

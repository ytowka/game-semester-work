package com.danilkha.client.api;

import org.danilkha.connection.Call;
import org.danilkha.connection.PackageReceiver;
import org.danilkha.protocol.Protocol;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApiCall<T> implements Call<T> {

    public final String request;
    public final int timeout;

    public final Call<T> callback;
    private final PackageReceiver packageReceiver;

    private AtomicBoolean isComplete = new AtomicBoolean(false);

    public ApiCall(String request, Call<T> callback, PackageReceiver packageReceiver, String... data) {
        this(request, callback, 5000, packageReceiver, data);
    }

    private final Thread timeoutChecker;
    private final Thread worker;

    public ApiCall(String request, Call<T> callback, int timeout, PackageReceiver packageReceiver, String... data) {
        this.request = request;
        this.timeout = timeout;
        this.callback = callback;
        this.packageReceiver = packageReceiver;

        timeoutChecker = new Thread(() -> {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException ignored) {

            }
            if(!isComplete.get()){
                onError(new TimeoutException());
            }
        });
        worker = new Thread(() -> {
            packageReceiver.receiveData(Protocol.buildGetRequest(request, data));
            isComplete.set(true);
        });
    }

    void execute(){
        worker.start();
        timeoutChecker.start();
    }

    @Override
    public void onSuccess(T data) {
        timeoutChecker.stop();
        callback.onSuccess(data);
    }

    @Override
    public void onError(Throwable t) {
        callback.onError(t);
    }
}

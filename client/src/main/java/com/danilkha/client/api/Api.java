package com.danilkha.client.api;

import org.danilkha.connection.Call;
import org.danilkha.connection.PackageReceiver;
import org.danilkha.connection.Response;
import org.danilkha.protocol.Protocol;
import org.danilkha.utils.observable.Completable;
import org.danilkha.utils.observable.MutableObservableValue;
import org.danilkha.utils.observable.ObservableValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Api implements PackageReceiver {

    private final Map<String, ApiCall<String[]>> calls;
    private final Map<String, MutableObservableValue<String[]>> subscribedKeys;

    private final MutableObservableValue<String> _rawInput = new MutableObservableValue<>();
    public final ObservableValue<String> rawInput = _rawInput;

    private final PackageReceiver server;

    public Api(PackageReceiver server){
        this.server = server;
        calls = new HashMap<>();
        subscribedKeys = new HashMap<>();
    }

    void get(String request, Call<String[]> call, String... data){
        ApiCall<String[]> stringCall = new ApiCall<>(request, call, server, data);
        calls.put(request, stringCall);
        stringCall.execute();
    }

    Completable<Boolean> post(String request, String... data){
        Completable<Boolean> completable = new Completable<>();
        ApiCall<String[]> stringCall = new ApiCall<>(request, new Call<>() {
            @Override
            public void onSuccess(String[] data) {
                completable.complete(true);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                completable.complete(false);
            }
        }, server, data);
        calls.put(request, stringCall);
        stringCall.execute();
        return completable;
    }

    void dropRaw(String data){
        String request = Protocol.DROP_PREFIX+data;
        server.receiveData(data);
    }

    void drop(String request, String... data){
        server.receiveData(Protocol.buildDropRequest(request, data));
    }

    ObservableValue<String[]> subscribe(String key, String... data){
        MutableObservableValue<String[]> stringObservableValue = new MutableObservableValue<>();
        subscribedKeys.put(key, stringObservableValue);
        server.receiveData(Protocol.buildSubscribeRequest(key, data));
        return stringObservableValue;
    }

    @Override
    public final void receiveData(String data) {
        Response response = Protocol.ParseResponse(data);
        System.out.println(response);

        switch (response.type()){
            case GIVE -> processGetResponse(response);
            case EMIT -> processSubscriptionResponse(response);
            case RAW -> _rawInput.setValue(response.data()[0]);
        }

    }

    private void processGetResponse(Response response){
        Call<String[]> call = calls.get(response.request());
        if(call != null){
            call.onSuccess(response.data());
            calls.remove(response.request());
        }
    }

    private void processSubscriptionResponse(Response response){
        MutableObservableValue<String[]> observableValue = subscribedKeys.get(response.request());
        if(observableValue != null){
            observableValue.setValue(response.data());
        }
    }
}

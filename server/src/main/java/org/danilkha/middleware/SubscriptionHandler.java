package org.danilkha.middleware;

import org.danilkha.connection.ClientRequest;
import org.danilkha.utils.observable.ObservableValue;

public interface SubscriptionHandler {
    ObservableValue<String[]> receiveRequest(ClientRequest clientRequest) ;
}

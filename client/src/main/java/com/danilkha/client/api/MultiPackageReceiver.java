package com.danilkha.client.api;

import org.danilkha.connection.PackageReceiver;

import java.util.ArrayList;
import java.util.List;

public class MultiPackageReceiver implements PackageReceiver {

    private List<PackageReceiver> childReceivers;

    public MultiPackageReceiver(){
        childReceivers = new ArrayList<>();
    }
    @Override
    public void receiveData(String data) {
        childReceivers.forEach(packageReceiver -> packageReceiver.receiveData(data));
    }

    public void addReceiver(PackageReceiver packageReceiver){
        childReceivers.add(packageReceiver);
    }

    public void removeReceiver(PackageReceiver packageReceiver){
        childReceivers.remove(packageReceiver);
    };
}

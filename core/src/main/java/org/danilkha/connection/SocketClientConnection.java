package org.danilkha.connection;

import org.danilkha.protocol.Protocol;
import org.danilkha.utils.coding.SbUtil;
import org.danilkha.utils.observable.ObservableValue;
import org.danilkha.utils.observable.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class SocketClientConnection implements PackageReceiver {
    private final Socket socket;

    private final PrintWriter writer;
    private final BufferedReader reader;

    private final Thread receiverThread;
    private final Thread emitterThread;

    private final PackageReceiver packageReceiver;

    private final ArrayBlockingQueue<String> messageQueue;

    private DisconnectListener disconnectListener = e -> {};

    private final List<DisposableObserver<?>> disposableObservers;

    public SocketClientConnection(Socket socket, PackageReceiver packageReceiver) throws IOException {
        this.socket = socket;

        receiverThread = new Thread(this::runReceiver);
        receiverThread.setName("input thread");
        emitterThread = new Thread(this::runEmitter);
        emitterThread.setName("output name");
        writer = new PrintWriter(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.packageReceiver = packageReceiver;
        messageQueue = new ArrayBlockingQueue<>(10);
        disposableObservers = new ArrayList<>();
    }

    private void runReceiver(){
        while (!socket.isClosed()){
            try {
                String data = reader.readLine();
                packageReceiver.receiveData(data);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                onDisconnect(e);
                break;
            }
        }
    }

    private void runEmitter(){
        while (!socket.isClosed()){
            try {
                String data = messageQueue.take();
                writer.println(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
                onDisconnect(e);
                break;
            }finally {
                writer.flush();
            }
        }
    }

    public void start(){
        receiverThread.start();
        emitterThread.start();
    }

    private void onDisconnect(Exception e){
        disposableObservers.forEach(DisposableObserver::dispose);
        disposableObservers.clear();
        disconnectListener.onDisconnect(e);
    }

    @Override
    public void receiveData(String data) {
        messageQueue.add(data);
    }

    public void setDisconnectListener(DisconnectListener disconnectListener){
        this.disconnectListener = disconnectListener;
    }

    public <T> void disposeOnDisconnect(ObservableValue<T> observableValue, Observer<T> observer){
        disposableObservers.add(new DisposableObserver<>(observableValue, observer));
    }

    public void stop() {
        try {
            messageQueue.add("");
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            onDisconnect(e);
        }
    }
}

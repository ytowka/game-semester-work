package org.danilkha.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class SocketClientConnection implements PackageReceiver {
    private final Socket socket;

    private final PrintWriter writer;
    private final BufferedReader reader;

    private final Thread receiverThread;
    private final Thread emitterThread;

    private final PackageReceiver packageReceiver;

    private final ArrayBlockingQueue<String> messageQueue;

    public SocketClientConnection(Socket socket, PackageReceiver packageReceiver) throws IOException {
        this.socket = socket;

        receiverThread = new Thread(this::runReceiver);
        emitterThread = new Thread(this::runEmitter);
        writer = new PrintWriter(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.packageReceiver = packageReceiver;
        messageQueue = new ArrayBlockingQueue<>(10);
    }

    private void runReceiver(){
        while (true){
            try {
                String data = reader.readLine();
                packageReceiver.receiveData(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void runEmitter(){
        while (true){
            try {
                String data = messageQueue.take();
                writer.println(data);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                writer.flush();
            }
        }
    }

    public void start(){
        receiverThread.start();
        emitterThread.start();
    }

    @Override
    public void receiveData(String data) {
        messageQueue.add(data);
    }
}
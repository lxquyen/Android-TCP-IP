package com.quyenlx.server;

import android.text.TextUtils;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by quyenlx on 11/16/2017.
 */

public class ServerBox {
    private static final String TAG = ServerBox.class.getSimpleName();
    private static ServerSocket serverSocket = null;
    private static final int maxClientsCount = 15;
    private static final Connection[] connections = new Connection[maxClientsCount];
    private final int portNumber;

    //client box
    private Socket boxClient;
    private DataInputStream is;
    private PrintStream os;
    private ServerBoxListener listener;

    public ServerBox(int portNumber) {
        this.portNumber = portNumber;
    }

    public void runServer(ServerBoxListener listener) {
        this.listener = listener;
        Observable
                .create(e -> {
                    serverSocket = new ServerSocket(portNumber);
                    initBoxClient();
                    while (!serverSocket.isClosed()) {
                        try {
                            Socket client = serverSocket.accept();
                            int i;
                            for (i = 0; i < maxClientsCount; i++) {
                                if (connections[i] == null) {
                                    connections[i] = new Connection(client, connections).run();
                                    break;
                                }
                            }
                            if (i == maxClientsCount) {
                                PrintStream os = new PrintStream(client.getOutputStream());
                                os.print("Server too busy. Try later.");
                                os.close();
                                client.close();
                            }
                        } catch (Exception ex) {
                            Timber.e(ex);
                        }

                    }
                    e.onNext(true);
                    e.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Timber.e(throwable))
                .doOnComplete(() -> serverSocket.close())
                .subscribe();


    }

    private void initBoxClient() {
        Timber.d("----------->initBoxClient");
        Observable
                .create(e -> {
                    boxClient = new Socket("localhost", portNumber);
                    os = new PrintStream(boxClient.getOutputStream());
                    is = new DataInputStream(boxClient.getInputStream());
                    handleBoxClientConnected();
                    while (boxClient.isConnected()) {
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        String line = new String(buffer, "UTF-8").trim();
                        if (!TextUtils.isEmpty(line)/* && line.startsWith("action")*/) {
                            handleBoxClientReceiver(line);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> boxClient.close())
                .subscribe();
    }

    private void handleBoxClientReceiver(String line) {
        Timber.d("------------> handleBoxClientReceiver: %s", line);
        Observable
                .create((ObservableOnSubscribe<String>) e -> {
                    e.onNext(line);
                    e.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> listener.onReceiver(message));
    }

    private void handleBoxClientConnected() {
        Timber.d("------------> handleBoxClientConnected: %s", boxClient.isConnected());
        Observable
                .create((ObservableOnSubscribe<Boolean>) e -> {
                    e.onNext(boxClient.isConnected());
                    e.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> listener.onConnected(isConnected));
    }

    public void destroy() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    public void sendMessageToAll(String message) {
        Observable
                .create(e -> {
                    os.print(message);
                    e.onNext(true);
                    e.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}

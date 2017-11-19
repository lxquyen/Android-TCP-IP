package com.quyenlx.server;

import android.text.TextUtils;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by quyenlx on 11/16/2017.
 */

public class Connection {
    private DataInputStream is;
    private PrintStream os;
    private final Socket socket;
    private final Connection[] connections;

    private int maxConnectionCount;

    public Connection(Socket socket, Connection[] connections) {
        this.socket = socket;
        this.connections = connections;
        this.maxConnectionCount = connections.length;
    }

    public Connection run() {
        Observable
                .create(e -> {
                    is = new DataInputStream(socket.getInputStream());
                    os = new PrintStream(socket.getOutputStream());
                    while (socket.isConnected()) {
                        byte[] buffer = new byte[is.available()];
                        is.read(buffer);
                        String line = new String(buffer, "UTF-8").trim();
                        if (!TextUtils.isEmpty(line)) {
                            Timber.d("--------->%s", line);
                            synchronized (this) {
                                for (int i = 0; i < maxConnectionCount; i++) {
                                    if (connections[i] != null && connections[i] != this) {
                                        connections[i].os.print(line);
                                    }
                                }
                            }
                        }
                    }
                    e.onNext(true);
                    e.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    is.close();
                    os.close();
                    socket.close();
                })
                .subscribe();
        return this;
    }
}

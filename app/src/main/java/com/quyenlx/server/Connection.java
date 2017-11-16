package com.quyenlx.server;

import android.text.TextUtils;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by quyenlx on 11/16/2017.
 */

public class Connection extends Thread {
    private static final String TAG = Connection.class.getSimpleName();
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

    @Override
    public void run() {
        try {
            is = new DataInputStream(socket.getInputStream());
            os = new PrintStream(socket.getOutputStream());

            os.println("Xin chào..ố. ế");
            while (socket.isConnected()) {
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                String line = new String(buffer, "UTF-8");
                if (!TextUtils.isEmpty(line)) {
                    Log.i(TAG, "--------->" + line);
                    synchronized (this) {
                        for (int i = 0; i < maxConnectionCount; i++) {
                            if (connections[i] != null) {
                                connections[i].os.println(line);
                            }
                        }
                    }
                }
            }

            synchronized (this) {
                for (int i = 0; i < maxConnectionCount; i++) {
                    if (connections[i] == this) {
                        connections[i] = null;
                    }
                }
            }

            is.close();
            os.close();
            socket.close();

        } catch (IOException e) {
        }
    }


}

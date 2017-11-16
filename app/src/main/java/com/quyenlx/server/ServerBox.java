package com.quyenlx.server;

import android.util.Log;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by quyenlx on 11/16/2017.
 */

public class ServerBox extends Thread {
    private static final String TAG = ServerBox.class.getSimpleName();
    private static ServerSocket serverSocket = null;
    private static final int maxClientsCount = 10;
    private static final Connection[] connections = new Connection[maxClientsCount];

    private final int portNumber;

    public ServerBox(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        init();
    }

    private void init() {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }

        while (true) {
            if (serverSocket.isClosed()) {
                break;
            }
            try {
                Socket client = serverSocket.accept();
                int i;
                for (i = 0; i < maxClientsCount; i++) {
                    if (connections[i] == null) {
                        ((connections[i] = new Connection(client, connections))).start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(client.getOutputStream());
                    os.print("Server too busy. Try later.");
                    os.close();
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopBox() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

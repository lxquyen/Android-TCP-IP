package com.quyenlx.server;

/**
 * Created by quyenlx on 11/19/2017.
 */

public interface ServerBoxListener {
    void onConnected(boolean isConnected);

    void onReceiver(String action);
}

package com.quyenlx.server;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by quyenlx on 11/16/2017.
 */

public class ClientAna extends AsyncTask<Void, String, Void> {
    private static final String TAG = ClientAna.class.getSimpleName();
    private final String ip;
    private final int port;

    private Socket client;
    private InputStream is;
    private PrintStream os;
    private final MessageCallback callback;

    ClientAna(String ip, int port, MessageCallback callback) {
        this.ip = ip;
        this.port = port;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            client = new Socket(ip, port);
            client.setSoTimeout(100);
            os = new PrintStream(client.getOutputStream());
            is = client.getInputStream();
            if (client.isConnected()) {
                callback.connected();
            }
            while (client.isConnected()) {
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                String line = new String(buffer, "UTF-8");
                if (!TextUtils.isEmpty(line.trim())) {
                    publishProgress(line);
                }
            }

        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        callback.receiver(values[0].trim());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            Log.i(TAG, "---------> disconnect");
            is.close();
            os.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(final String message) {
        Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                        os.println(message);
                        e.onNext(true);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    interface MessageCallback {
        void connected();

        void receiver(String message);
    }

}

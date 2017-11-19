package com.quyenlx.server;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by quyenlx on 11/19/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}

package com.example.puzle_15_mukammal;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalStorage.init(this);
        Records.init(this);

    }
}

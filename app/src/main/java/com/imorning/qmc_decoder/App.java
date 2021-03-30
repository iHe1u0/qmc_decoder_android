package com.imorning.qmc_decoder;

import android.app.Application;

import com.hjq.permissions.XXPermissions;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        XXPermissions.setScopedStorage(true);
    }
}

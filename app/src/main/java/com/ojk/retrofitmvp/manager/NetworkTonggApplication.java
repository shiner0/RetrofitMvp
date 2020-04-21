package com.ojk.retrofitmvp.manager;

import android.app.Application;

/**
 * 网络application
 */
public class NetworkTonggApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        NetworkTonggMonitorReceiver.getInstance().init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        NetworkTonggMonitorReceiver.getInstance().destory();
    }

}

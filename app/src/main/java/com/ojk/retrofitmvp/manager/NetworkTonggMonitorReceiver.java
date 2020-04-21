package com.ojk.retrofitmvp.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class NetworkTonggMonitorReceiver extends BroadcastReceiver {
    private static final String TAG = "SNetWorkMonitorReceiver";
    private static NetworkTonggMonitorReceiver instance;
    private NetworkTonggObservable observable;


    public void addObserver(NetworTonggServer observer) {
        this.observable.addObserver(observer);
    }

    public void delObserver(NetworTonggServer observer) {
        this.observable.deleteObserver(observer);
    }

    public void destory() {
        this.observable.deleteObservers();
    }

    public static NetworkTonggMonitorReceiver getInstance() {
        if (instance == null) {
            synchronized (NetworkTonggMonitorReceiver.class) {
                if (instance == null) {
                    instance = new NetworkTonggMonitorReceiver();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.observable = new NetworkTonggObservable(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(this, intentFilter);
    }

    private void notifyNetState(Context context) {
        try {
            android.net.NetworkInfo networkInfo = NetworkTonggInfo.getCurrentActiveNetwork(context);
            if (networkInfo != null) {
                if (!networkInfo.isAvailable()) {
                    this.observable.notifyObservers(new NetworTonggServer.NetAction(false, false, NetworkTonggInfo.getSubType(context)));
                    return;
                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    this.observable.notifyObservers(new NetworTonggServer.NetAction(true, true, NetworkTonggInfo.getSubType(context)));
                    return;
                }

                this.observable.notifyObservers(new NetworTonggServer.NetAction(true, false, NetworkTonggInfo.getSubType(context)));
                return;
            }

            this.observable.notifyObservers(new NetworTonggServer.NetAction(false, false, NetworkTonggInfo.getSubType(context)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.notifyNetState(context);
    }
}


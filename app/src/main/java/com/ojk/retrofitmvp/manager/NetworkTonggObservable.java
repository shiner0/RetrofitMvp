package com.ojk.retrofitmvp.manager;

/**
 * Author   Shone
 * Date     04/07/16.
 * Github   https://github.com/shonegg
 */

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.Observable;
import java.util.Observer;

public class NetworkTonggObservable extends Observable {
    private Context context;

    public NetworkTonggObservable(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void addObserver(Observer observer) {
        try {
            super.addObserver(observer);
            android.net.NetworkInfo networkInfo = NetworkTonggInfo.getCurrentActiveNetwork(this.context);
            if (networkInfo != null) {
                if (!networkInfo.isAvailable()) {
                    observer.update(this, new NetworTonggServer.NetAction(false, false, NetworkTonggInfo.getSubType(context)));
                    return;
                }

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    observer.update(this, new NetworTonggServer.NetAction(true, true, NetworkTonggInfo.getSubType(context)));
                    return;
                }

                observer.update(this, new NetworTonggServer.NetAction(true, false, NetworkTonggInfo.getSubType(context)));
                return;
            }

            observer.update(this, new NetworTonggServer.NetAction(false, false, NetworkTonggInfo.getSubType(context)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers(Object data) {
        try {
            this.setChanged();
            super.notifyObservers(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
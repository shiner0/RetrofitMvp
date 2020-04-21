package com.ojk.retrofitmvp.manager;

/**
 * Author   Shone
 * Date     04/07/16.
 * Github   https://github.com/shonegg
 */

import java.util.Observable;
import java.util.Observer;

public abstract class NetworTonggServer implements Observer {
    public static class NetAction {
        private boolean isAvailable;
        private boolean isWifi;
        private NetworkTonggInfo.Type type;

        public NetAction(boolean isAvailable, boolean isWifi, NetworkTonggInfo.Type type) {
            super();
            this.isAvailable = isAvailable;
            this.isWifi = isWifi;
            this.type = type;
        }

        public boolean isAvailable() {
            return this.isAvailable;
        }

        public NetworkTonggInfo.Type getType() {
            return type;
        }

        public void setType(NetworkTonggInfo.Type type) {
            this.type = type;
        }

        public boolean isWifi() {
            return this.isWifi;
        }
    }

    public abstract void notify(NetAction action);

    @Override
    public void update(Observable observable, Object data) {
        this.notify(((NetAction) data));
    }
}

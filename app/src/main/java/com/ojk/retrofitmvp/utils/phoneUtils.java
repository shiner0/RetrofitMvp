package com.ojk.retrofitmvp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.ojk.retrofitmvp.entity.Headinfo;
import com.ojk.retrofitmvp.manager.HomeTonggApplication;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import androidx.core.app.ActivityCompat;

public class phoneUtils {

    public static String getHeaders() {

        String deviceInfo = "";
        Headinfo mHeadInfo = new Headinfo();
        PackageManager pm = HomeTonggApplication.getInstance().getPackageManager();
        try {
            PackageInfo pi = null;
            pi = pm.getPackageInfo(HomeTonggApplication.getInstance().getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                mHeadInfo.operationSys = "android";
                mHeadInfo.appVersion = versionName;
                mHeadInfo.channel = HomeTonggApplication.getChannelName();
                mHeadInfo.brand = Build.MANUFACTURER;
                mHeadInfo.deviceModel = Build.MODEL;
//                TelephonyManager tm = (TelephonyManager) HomeTonggApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
//                if (ActivityCompat.checkSelfPermission(HomeTonggApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                    return "";
//                }
                mHeadInfo.udid = "Adadwf1162";
                mHeadInfo.mac = getNewMac();
                mHeadInfo.osVersion = Build.VERSION.RELEASE;
                mHeadInfo.bag = pi.packageName;
                mHeadInfo.appsflyer_id = "";
                mHeadInfo.advertising_id = "";
            }
            deviceInfo = new Gson().toJson(mHeadInfo);
            if (deviceInfo.equals("")) {
                mHeadInfo.channel = HomeTonggApplication.getChannelName();
                mHeadInfo.bag = pi.packageName;
            }
            return new Gson().toJson(mHeadInfo);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 通过网络接口取
     *
     * @return
     */
    public static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }



}

package com.ojk.retrofitmvp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class HappyNewSharedPreferencesUtil {


    public static SharedPreferences share(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("danasetia", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static SharedPreferences shareLaunch(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launch", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void clearSp(Context context) {
        share(context).edit().clear().commit();
    }

    public static String getToken(Context context) {
        return share(context).getString("token", "");
    }

    public static void setToken(String value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putString("token", value);
        e.apply();
    }

    public static String getPhone(Context context) {
        return share(context).getString("phoneNum", "");
    }

    public static void setPhone(String value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putString("phoneNum", value);
        e.apply();
    }

    public static String getChannel(Context context) {
        return share(context).getString("channel", "googleplay");
    }

    public static void setChannel(String value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putString("channel", value);
        e.apply();
    }

    public static String getGoogleId(Context context) {
        return share(context).getString("googleId", "");
    }

    public static void setGoogleId(String value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putString("googleId", value);
        e.apply();
    }

    public static boolean getBindGmail(Context context) {
        return share(context).getBoolean("bindGmail", false);
    }

    public static void setBindGmail(boolean value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putBoolean("bindGmail", value);
        e.apply();
    }
    public static long getLaunchTime(Context context) {
        return shareLaunch(context).getLong("launchTime", 0);
    }

    public static void setLaunchTime(long value, Context context) {
        SharedPreferences.Editor e = shareLaunch(context).edit();
        e.putLong("launchTime", value);
        e.apply();
    }

    public static long getUid(Context context) {
        return share(context).getLong("uid", 0);
    }

    public static void setUid(long value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putLong("uid", value);
        e.apply();
    }

    public static boolean getGoogle(Context context) {
        return share(context).getBoolean("googlebind", false);
    }

    public static void setGoogle(boolean value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putBoolean("googlebind", value);
        e.apply();
    }


    public static boolean getDaiChao(Context context) {
        return shareLaunch(context).getBoolean("daichao", false);
    }

    public static void setDaiChao(boolean value, Context context) {
        SharedPreferences.Editor e = shareLaunch(context).edit();
        e.putBoolean("daichao", value);
        e.apply();
    }

    public static boolean getPrivacy(Context context) {
        return shareLaunch(context).getBoolean("privacy", false);
    }

    public static void setPrivacy(boolean value, Context context) {
        SharedPreferences.Editor e = shareLaunch(context).edit();
        e.putBoolean("privacy", value);
        e.apply();
    }


    public static int getLoginType(Context context) {
        return share(context).getInt("loginType", 0);
    }

    public static void setLoginType(int value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putInt("loginType", value);
        e.apply();
    }


    public static long getNoticeId(Context context) {
        return share(context).getLong("noticeid", 0);
    }

    public static void setNoticeId(long value, Context context) {
        SharedPreferences.Editor e = share(context).edit();
        e.putLong("noticeid", value);
        e.apply();
    }

}

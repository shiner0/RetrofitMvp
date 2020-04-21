package com.ojk.retrofitmvp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import com.ojk.retrofitmvp.manager.HomeTonggApplication;

;

/**
 * @brief UI相关的处理工具类 .
 */
public class UITonggUtils {

    private UITonggUtils() {
    }

    public static Context getContext() {
        return HomeTonggApplication.getContext();
    }


    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static long getMainThreadId() {
        return HomeTonggApplication.getMainThreadId();
    }

    /**
     * 判断当前的线程是不是在主线程
     *
     * @return
     */
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return HomeTonggApplication.getMainThreadHandler();
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }


    public static void handleFailure(Context context) {
            ToastTonggUtils.show("Pengecualian jaringan, silakan coba lagi nanti");
    }



}

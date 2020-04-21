package com.ojk.retrofitmvp.utils;

import android.widget.Toast;

import com.ojk.retrofitmvp.manager.HomeTonggApplication;

import androidx.annotation.StringRes;

/**
 * 描述:Toast工具类
 */
public class ToastTonggUtils {

    private ToastTonggUtils() {
    }

    public static void show(CharSequence text) {
        if (text.length() < 10) {
            Toast.makeText(HomeTonggApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HomeTonggApplication.getInstance(), text, Toast.LENGTH_LONG).show();
        }
    }

    public static void show(@StringRes int resId) {
        show(HomeTonggApplication.getInstance().getString(resId));
    }


    /** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
    public static void showToastSafe(final int resId) {
        showToastSafe(UITonggUtils.getString(resId));
    }

    /** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
    public static void showToastSafe(final String str) {
        if (UITonggUtils.isRunInMainThread()) {
            show(str);
        } else {
            UITonggUtils.post(new Runnable() {
                @Override
                public void run() {
                    show(str);
                }
            });
        }
    }

}
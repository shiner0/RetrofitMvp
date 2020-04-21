package com.ojk.retrofitmvp.manager;

import android.app.Activity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;


final public class AppInfoTonggManager {
    private static ArrayList<SoftReference<Activity>> sActivityStack;
    private static AppInfoTonggManager sInstance;

    private OnAllActivityClosed mActivityClosed;

    // Activity栈的MaxSize，为0表示不限制
    private int mActivityStackMaxSize = 0;


    private AppInfoTonggManager() {
        if (sActivityStack == null) {
            sActivityStack = new ArrayList<SoftReference<Activity>>(20);
        }
    }

    /**
     * 得到单例
     *
     * @return
     */
    public static AppInfoTonggManager getInst() {
        if (sInstance == null) {
            sInstance = new AppInfoTonggManager();
        }
        return sInstance;
    }

    /**
     * 得到当前Activity栈里面的管理的Activity的数量
     *
     * @return
     */
    public int getSize() {
        return sActivityStack.size();
    }

    /**
     * 向Activity栈中压入一个Activity
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activity != null) {
            sActivityStack.add(new SoftReference<Activity>(activity));
            checkAndMaintainActivityStack(mActivityStackMaxSize);
        }
    }

    /**
     * 从Activity栈中移除最上面的Activity
     *
     * @return 返回移除的Activity
     */
    public Activity popActivity() {
        int size = sActivityStack.size();
        if (size == 0) {
            return null;
        }
        SoftReference<Activity> sr = sActivityStack.remove(size - 1);
        if (sr == null) {
            return null;
        }
        Activity activity = sr.get();
        // BdLog.d("Activity popped: " + activity);
        return activity;
    }

    /**
     * 从Activity栈中移除某个index对应的Activity
     *
     * @param index 索引
     * @return 返回移除的Activity
     */
    public Activity popActivity(int index) {
        int size = sActivityStack.size();
        if (size == 0) {
            return null;
        }
        if (index < 0 || index >= size) {
            return null;
        }
        SoftReference<Activity> sr = sActivityStack.remove(index);
        if (sr == null) {
            return null;
        }
        Activity activity = sr.get();
        return activity;
    }

    private void checkAndMaintainActivityStack(int activityStackMaxSize) {
        if (activityStackMaxSize == 0) {
            return;
        }

        int currentSize = AppInfoTonggManager.getInst().getSize();
        while (currentSize > activityStackMaxSize) {
            currentSize--;
            // remove the second bottom.
            Activity act = AppInfoTonggManager.getInst().popActivity(1);
            if (act != null) {
                act.finish();
            }
        }
    }

    /**
     * Activity全部关闭时调用的接口
     *
     * @author libin18
     */
    public interface OnAllActivityClosed {
        public void onActivityClosed();
    }


}

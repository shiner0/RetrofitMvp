package com.ojk.retrofitmvp.manager;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.DisplayMetrics;
import com.ojk.retrofitmvp.utils.CommonTonggParams;

import androidx.multidex.MultiDex;


public class HomeTonggApplication extends NetworkTonggApplication {

    private static Context mContext;

    private static HomeTonggApplication mYhmApplicationInstance;

    private static Handler mMainThreadHandler = null;

    private static int mMainThreadId;

    private NotificationReceiver mNotificationReceiver;


    public static int screenWidth;
    public static int screenHeight;

    public HomeTonggApplication() {
        mYhmApplicationInstance = this;
    }

    public static HomeTonggApplication getInstance() {
        return mYhmApplicationInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mYhmApplicationInstance = this;
        mMainThreadHandler = new Handler();
        mMainThreadId = android.os.Process.myTid();
        initListener();

        DisplayMetrics mDisplayMetrics = getApplicationContext().getResources()
                .getDisplayMetrics();

        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;

    }


    /**
     * 全局的广播接收者,用于处理登录后数据的操作
     */
    private class NotificationReceiver extends BroadcastReceiver {//此广播会有延时，在进入界面后会有获取不到登陆人信息的的情况

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equalsIgnoreCase(CommonTonggParams.USER_LOGOUT)) {
                onLogout();
            } else if (action.equalsIgnoreCase(CommonTonggParams.LOGIN_ACTIVITY_FINISHED)) {
                onLoginSuccess();
            }
        }
    }


    private void initListener() {
        mNotificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(CommonTonggParams.USER_LOGOUT);
        filter.addAction(CommonTonggParams.LOGIN_ACTIVITY_FINISHED);
        registerReceiver(mNotificationReceiver, filter);
    }

    public static Context getContext() {
        return mContext;
    }

    public static HomeTonggApplication getYhmApplicationInstance() {
        if (mYhmApplicationInstance == null) {
            mYhmApplicationInstance = new HomeTonggApplication();
        }
        return mYhmApplicationInstance;
    }


    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 登录成功后执行的操作
     */
    protected void onLoginSuccess() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("lang", "id");
//        headers.put("deviceInfo", ToolGsonUtils.beantoJson(mHeadInfo));
//        headers.put("token", UITonggUtils.getUserToken(this));
//        OkGo.getInstance().addCommonHeaders(headers);
    }


    /**
     * 退出登录后执行的操作
     */
    private void onLogout() {
        //删除token和login
        SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences("uangtechconfig", Activity.MODE_PRIVATE).edit();
        edit.remove("token");
        edit.remove(CommonTonggParams.USER_INFO);
        //清除保存的筛选数据
        edit.commit();
    }

    public static String getChannelName() {
        String channelName = "whitelist";
     //   channelName = ToolSharedPreferencesUtils.getString(mContext, "channelName", "shujubg");
        try {
            PackageManager packageManager = mYhmApplicationInstance.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mYhmApplicationInstance.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //此处这样写的目的是为了在debug模式下也能获取到渠道号，如果用getString的话只能在Release下获取到。
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }
}

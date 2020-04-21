package com.ojk.retrofitmvp.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gyf.immersionbar.ImmersionBar;
import com.ojk.retrofitmvp.R;
import com.ojk.retrofitmvp.manager.AppInfoTonggManager;
import com.ojk.retrofitmvp.manager.NetworTonggServer;
import com.ojk.retrofitmvp.manager.NetworkTonggMonitorReceiver;
import com.ojk.retrofitmvp.utils.CheckTonggPermissions;
import com.ojk.retrofitmvp.utils.CommonTonggParams;
import com.ojk.retrofitmvp.utils.ToastTonggUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author azheng
 * @date 2018/4/24.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public abstract class BaseTonggActivity extends AppCompatActivity implements CheckTonggPermissions.PermissionCallbacks{


    private Unbinder unbinder;
    private Thread mNetThread;
    protected Activity context = null;
    public RequestManager requestManager;
    public int iswifi = 0;
    private boolean stopThread = false;
    protected static final int RC_PERM = 123;
    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();


    private interface OnCompleteListener {
        void onComplete();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);  //防止软键盘遮挡输入框
        setContentView(this.getLayoutResId());
        context = this;
        NetworkTonggMonitorReceiver.getInstance().addObserver(this.mNetObserver);
        startThreadReLoadData();
        unbinder = ButterKnife.bind(this);

        requestManager = Glide.with(this);
        AppInfoTonggManager.getInst().pushActivity(this);
        ImmersionBar.with(this).init();
        initView();
        initData();
    }


    private NetworTonggServer mNetObserver = new NetworTonggServer() {
        @Override
        public void notify(NetworTonggServer.NetAction action) {
            if (action.isAvailable()) {
          //      ToolSharedPreferencesUtils.saveboolean(context, "hasNet", true);
                if (action.isWifi()) {
                    iswifi = 1;
                } else {
                    iswifi = 0;
                }
                System.out.println("当前网络是：" + action.getType() + "是否是wifi:" + iswifi);
            } else {
            //    ToolSharedPreferencesUtils.saveboolean(context, "hasNet", false);
             //   ToolSharedPreferencesUtils.saveboolean(context, "notNet", true);
                showNetError();
            }
        }
    };


    /**
     * 网络连接失败提示
     */
    protected void showNetError() {
//        new AlertView(UITonggUtils.getString(R.string.tip), UITonggUtils.getString(R.string.net_error),
//                null, new String[]{UITonggUtils.getString(R.string.queding)}, null, this,
//                AlertView.Style.Alert, null).show();
    }

    /**
     * 开启一个线程，监测网络由无到有后，重新加载数据
     */
    private void startThreadReLoadData() {
        mNetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopThread) {
//                    if (ToolSharedPreferencesUtils.getboolean(context, "hasNet", false) && ToolSharedPreferencesUtils.getboolean(context, "hasNet", false)) {
//                        ToolSharedPreferencesUtils.saveboolean(context, "notNet", false);
//                        Log.e("有网了", "有网了。。。。");
//                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mNetThread.start();
    }


    protected void unRegisterBroadcastReceiver(Context context) {
        Intent mIntents = new Intent(CommonTonggParams.UNREGISTER_BROADCAST_RECEIVER);
        context.sendBroadcast(mIntents);
    }



    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @TargetApi(23)
    @SuppressWarnings("unused")
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final @NonNull String permissions[],
                                           final @NonNull int[] grantResults) {
        final OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
        if (permissionsListener != null
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsListener.onComplete();
        }
        CheckTonggPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 权限回调接口
     */
    private CheckPermListener mListener;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    public void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
        mListener = listener;
        if (CheckTonggPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            CheckTonggPermissions.requestPermissions(this, getString(resString),
                    RC_PERM, mPerms);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //同意了某些权限可能不是全部
    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
            mListener.superPermission();//同意了全部权限的回调
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        CheckTonggPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.perm_tip),
                R.string.setting, R.string.cancel, null, perms);
        cancelPermission();
    }

    private List<String> list;

    protected void getPermissions() {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(Manifest.permission.READ_CONTACTS);
        if (this.isFinishing()) {
            CheckTonggPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.get_some_premission),
                    R.string.setting, R.string.cancel, null, list);
        }
    }

    protected void cancelPermission() {
    }


    protected void showToast(String text) {
        ToastTonggUtils.showToastSafe(text);
    }
    protected void showToast(int resId) {
        ToastTonggUtils.showToastSafe(resId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread = true;
        NetworkTonggMonitorReceiver.getInstance().delObserver(this.mNetObserver);
        AppInfoTonggManager.getInst().popActivity();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayoutResId();

    /**
     * 初始化视图
     */
    public abstract void initView();
    /**
     * 加载数据
     */
    public abstract void initData();


}

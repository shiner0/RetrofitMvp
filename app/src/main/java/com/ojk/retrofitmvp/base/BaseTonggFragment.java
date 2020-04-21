package com.ojk.retrofitmvp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ojk.retrofitmvp.R;
import com.ojk.retrofitmvp.manager.HomeTonggApplication;
import com.ojk.retrofitmvp.manager.NetworTonggServer;
import com.ojk.retrofitmvp.manager.NetworkTonggMonitorReceiver;
import com.ojk.retrofitmvp.utils.CheckTonggPermissions;
import com.ojk.retrofitmvp.utils.CommonTonggParams;
import com.ojk.retrofitmvp.utils.ToastTonggUtils;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author azheng
 * @date 2018/4/24.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public abstract class BaseTonggFragment extends Fragment implements CheckTonggPermissions.PermissionCallbacks {

    private Unbinder unBinder;

    private Thread mNetThread;
    private boolean stopThread = false;
    protected Activity activity;
    public RequestManager requestManager;
    private int iswifi = 0;
    private boolean isVisible;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载
    protected static final int RC_PERM = 123;
    protected HomeTonggApplication mApp;

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = androidx.fragment.app.Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.getLayoutResId(), container, false);
        unBinder = ButterKnife.bind(this, view);
        mApp = HomeTonggApplication.getInstance();
        NetworkTonggMonitorReceiver.getInstance().addObserver(this.mNetObserver);
        startThreadReLoadData();
        requestManager = Glide.with(activity);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }


    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }



    protected void showToast(String text) {
        ToastTonggUtils.showToastSafe(text);
    }

    protected void showToast(int resId) {
        ToastTonggUtils.showToastSafe(resId);
    }

    /**
     * 开启一个线程，监测网络由无到有后，重新加载数据
     */
    private void startThreadReLoadData() {
        mNetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopThread) {
//                    if (ToolSharedPreferencesUtils.getboolean(getActivity(), "hasNet", false) && ToolSharedPreferencesUtils.getboolean(getActivity(), "hasNet", false)) {
//                        ToolSharedPreferencesUtils.saveboolean(getActivity(), "notNet", false);
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

    private NetworTonggServer mNetObserver = new NetworTonggServer() {
        @Override
        public void notify(NetAction action) {
            if (action.isAvailable()) {

             //   ToolSharedPreferencesUtils.saveboolean(getActivity(), "hasNet", true);
                if (action.isWifi()) {
                    iswifi = 1;
                } else {
                    iswifi = 0;
                }
                System.out.println("当前的网络是：" + action.getType() + "是否是wifi:" + iswifi);
            } else {

              //  ToolSharedPreferencesUtils.saveboolean(getActivity(), "hasNet", false);
              //  ToolSharedPreferencesUtils.saveboolean(getActivity(), "notNet", true);
                showNetError();
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkTonggMonitorReceiver.getInstance().delObserver(this.mNetObserver);
        stopThread = true;
    }


    public void showNetError() {
//        new AlertView(UITonggUtils.getString(R.string.tip), UITonggUtils.getString(R.string.net_error),
//                null, new String[]{UITonggUtils.getString(R.string.sure)}, null, activity,
//                AlertView.Style.Alert, null).show();
    }


    protected void unRegisterBroadcastReceiver(Context context) {
        Intent mIntents = new Intent(CommonTonggParams.UNREGISTER_BROADCAST_RECEIVER);
        context.sendBroadcast(mIntents);
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
        if (CheckTonggPermissions.hasPermissions(getActivity(), mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            CheckTonggPermissions.requestPermissions(this, getString(resString),
                    RC_PERM, mPerms);
        }
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        CheckTonggPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
    }

    /**
     * 初始化视图
     *
     * @param view
     */

    protected abstract void initView(View view);


    protected abstract void initData();


    protected abstract int getLayoutResId();
}

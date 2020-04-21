package com.ojk.retrofitmvp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ojk.retrofitmvp.base.BaseMvpTonggActivity;
import com.ojk.retrofitmvp.contract.MessageTonggContract;
import com.ojk.retrofitmvp.entity.CommonTonggEntity;
import com.ojk.retrofitmvp.presenter.MessageTonggPresenter;
import com.ojk.retrofitmvp.utils.ProgressTonggDialog;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseMvpTonggActivity<MessageTonggPresenter> implements MessageTonggContract.View {


    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mPresenter = new MessageTonggPresenter();
        mPresenter.attachView(this);

        Map<String, String> mParams = new HashMap<>();
        mParams.put("pushToken", "aaasd");
        mPresenter.pushToken(mParams);
    }

    @Override
    public void onPushToken(CommonTonggEntity bean) {

    }

    @Override
    public void showLoading() {
        ProgressTonggDialog.getInstance().show(this);
    }

    @Override
    public void hideLoading() {
        ProgressTonggDialog.getInstance().dismiss();
    }

    @Override
    public void onError(Throwable throwable, String type) {

    }

}

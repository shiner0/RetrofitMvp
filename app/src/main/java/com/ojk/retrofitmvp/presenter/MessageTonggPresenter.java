package com.ojk.retrofitmvp.presenter;


import com.ojk.retrofitmvp.base.BaseTonggPresenter;
import com.ojk.retrofitmvp.contract.MessageTonggContract;
import com.ojk.retrofitmvp.entity.CommonTonggEntity;
import com.ojk.retrofitmvp.model.MessageTonggModel;
import com.ojk.retrofitmvp.net.RxTonggScheduler;
import com.ojk.retrofitmvp.utils.FixedTonggUtils;

import java.util.Map;

import io.reactivex.functions.Consumer;


public class MessageTonggPresenter extends BaseTonggPresenter<MessageTonggContract.View> implements MessageTonggContract.Presenter {

    private MessageTonggContract.Model model;

    public MessageTonggPresenter() {
        model = new MessageTonggModel();
    }


    @Override
    public void pushToken(Map<String, String> map) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();

        model.pushToken(FixedTonggUtils.sign(map))
                .compose(RxTonggScheduler.<CommonTonggEntity>Flo_io_main())
                .as(mView.<CommonTonggEntity>bindAutoDispose())
                .subscribe(new Consumer<CommonTonggEntity>() {
                    @Override
                    public void accept(CommonTonggEntity bean) throws Exception {
                        mView.onPushToken(bean);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable, "pushToken");
                        mView.hideLoading();
                    }
                });
    }


}

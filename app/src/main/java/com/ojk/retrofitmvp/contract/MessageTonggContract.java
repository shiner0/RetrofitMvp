package com.ojk.retrofitmvp.contract;

import com.ojk.retrofitmvp.base.BaseTonggView;
import com.ojk.retrofitmvp.entity.CommonTonggEntity;

import java.util.Map;

import io.reactivex.Flowable;


public interface MessageTonggContract {
    interface Model {

        Flowable<CommonTonggEntity> pushToken(Map<String, String> map);

    }

    interface View extends BaseTonggView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable, String type);


        void onPushToken(CommonTonggEntity bean);


    }

    interface Presenter {


        void pushToken(Map<String, String> map);


    }
}

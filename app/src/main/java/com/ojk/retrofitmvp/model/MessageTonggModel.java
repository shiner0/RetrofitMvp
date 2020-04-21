package com.ojk.retrofitmvp.model;



import com.ojk.retrofitmvp.contract.MessageTonggContract;
import com.ojk.retrofitmvp.entity.CommonTonggEntity;
import com.ojk.retrofitmvp.net.RetrofitTonggClient;

import java.util.Map;

import io.reactivex.Flowable;

public class MessageTonggModel implements MessageTonggContract.Model {


    @Override
    public Flowable<CommonTonggEntity> pushToken(Map<String, String> map) {
        return RetrofitTonggClient.getInstance().getApi().pushToken(map);
    }


}

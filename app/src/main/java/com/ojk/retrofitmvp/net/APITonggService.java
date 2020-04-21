package com.ojk.retrofitmvp.net;

import com.ojk.retrofitmvp.entity.CommonTonggEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APITonggService {

    /**
     * 上传推送token
     */
    @FormUrlEncoded
    @POST("v1/user/fcmToken")
    Flowable<CommonTonggEntity> pushToken(@FieldMap Map<String, String> params);

}

package com.beini.net;

import android.support.annotation.NonNull;

import com.beini.bean.UserInfo;
import com.beini.net.request.UserRequest;
import com.beini.net.response.BaseResponseJson;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by beini on 2017/4/14.
 */

public interface RxReServer<T> {
    /**
     * @FormUrlEncoded: 表单的方式传递键值对
     */
    @POST("{url}")
    @NonNull
    Flowable<BaseResponseJson> insertRxUserRequest(@Path("url") String url, @Body UserRequest baseRequestJson);

    @POST("{url}")
    Call<BaseResponseJson> sendRequest(@Path("url") String url, @Body Object baseRequestJson);

    //标准模式
    @FormUrlEncoded
    @POST("account/login")
    Observable<UserInfo> login(
            @Field("userId") String userId,
            @Field("password") String password
    );

}

package com.beini.net;

import android.support.annotation.NonNull;

import com.beini.bean.BaseEntity;
import com.beini.bean.UserInfo;
import com.beini.bean.VideoUrl;
import com.beini.net.request.UserRequest;
import com.beini.net.response.BaseResponseJson;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Observable<BaseEntity<UserInfo>> login(
            @Field("userId") String userId,
            @Field("password") String password
    );

    @GET("video/getUrl")
    Observable<BaseEntity<VideoUrl>> getVideoUrl(
            @Query("id") long id
    );

    @FormUrlEncoded
    @POST("user/addVideo")
    Observable<BaseEntity<Boolean>> addVideo(
            @FieldMap Map<String, Object> map
    );

}

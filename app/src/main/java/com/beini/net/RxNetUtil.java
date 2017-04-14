package com.beini.net;

import com.beini.constants.NetConstants;
import com.beini.net.request.BaseRequestJson;
import com.beini.net.request.UserRequest;
import com.beini.net.response.BaseResponseJson;
import com.beini.utils.BLog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beini on 2017/4/14.
 */

public class RxNetUtil<T> {
    private static RxNetUtil instance;
    private static Retrofit retrofit;
    private static RxReServer rxReServer;
    private static int DEFAULT_TIMEOUT = 5;

    public static RxNetUtil getSingleton() {
        if (instance == null) {
            synchronized (RxNetUtil.class) {
                if (instance == null) {
                    instance = new RxNetUtil();
                    OkHttpClient client = new OkHttpClient//添加头信息，cookie等
                            .Builder().connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                    retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl(NetConstants.baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())//compile 'com.squareup.retrofit2:converter-gson:2.0.2'
                            .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                            .build();
                    rxReServer = retrofit.create(RxReServer.class);
                }
            }
        }
        return instance;
    }

    public void insertRxUserRequest(final UserRequest userRequest, final ResourceSubscriber<BaseResponseJson> subscriber, Scheduler scheduler) {
        Flowable<BaseResponseJson> flowable = rxReServer.insertRxUserRequest("insertUserM", userRequest);
        flowable.subscribeOn(Schedulers.io()).observeOn(scheduler).subscribe(subscriber);
    }

    public void sendRequest(final String url, final Object userRequest, final ResourceSubscriber<BaseResponseJson> subscriber, Scheduler scheduler) {
        Flowable.create(new FlowableOnSubscribe<BaseResponseJson>() {
            @Override
            public void subscribe(FlowableEmitter<BaseResponseJson> baseResponseJson) throws Exception {
                try {
                    Response baseResponseJsonResponse = rxReServer.inserUserRequest(url, userRequest).execute();
                    if (baseResponseJsonResponse.body() == null) {
                        subscriber.onError(new Throwable("error"));
                    } else {
                        subscriber.onNext((BaseResponseJson) baseResponseJsonResponse.body());
                        subscriber.onComplete();
                    }
                } catch (IOException e) {
                    subscriber.onError(new Throwable("error"));
                    e.printStackTrace();
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(scheduler)
                .subscribe(subscriber);
    }

}

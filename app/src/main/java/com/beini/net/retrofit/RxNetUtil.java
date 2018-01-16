package com.beini.net.retrofit;

import android.support.annotation.NonNull;

import com.beini.bean.UserInfo;
import com.beini.constants.NetConstants;
import com.beini.net.request.UserRequest;
import com.beini.net.response.BaseResponseJson;
import com.beini.net.server.RxApiServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beini on 2017/4/14.
 */

public class RxNetUtil {
    private static RxNetUtil instance;
    private static Retrofit retrofit;
    private static RxApiServer rxReServer;
    private static int DEFAULT_TIMEOUT = 5;

    public static RxNetUtil getSingleton() {
        if (instance == null) {
            synchronized (RxNetUtil.class) {
                if (instance == null) {
                    instance = new RxNetUtil();
                    OkHttpClient client = new OkHttpClient//添加头信息，cookie等
                            .Builder()
                            // 添加通用的Header
//                            .addInterceptor(new Interceptor() {
//                                @Override
//                                public okhttp3.Response intercept(Chain chain) throws IOException {
//                                    Request.Builder builder = chain.request().newBuilder();
//                                    builder.addHeader("token", "123");
//                                    return chain.proceed(builder.build());
//                                }
//                            })
                              /*
              这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
            出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
             */
//                            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//                                @Override
//                                public void log(String message) {
//
//                                }
//                            }).setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                    retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl(NetConstants.baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())//compile 'com.squareup.retrofit2:converter-gson:2.0.2'
                            // 添加Retrofit到RxJava的转换器
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                    rxReServer = retrofit.create(RxApiServer.class);
                }
            }
        }
        return instance;
    }

    public void insertRxUserRequest(final UserRequest userRequest, final ResourceSubscriber<BaseResponseJson> subscriber, Scheduler scheduler) {
        Flowable<BaseResponseJson> flowable = rxReServer.insertRxUserRequest("insertUserM", userRequest);
        flowable.subscribeOn(Schedulers.io()).observeOn(scheduler).subscribe(subscriber);
    }

    public void sendRequest(@NonNull final String url, @NonNull final Object userRequest, @NonNull final ResourceSubscriber<BaseResponseJson> subscriber, @NonNull Scheduler scheduler) {
        Flowable.create(new FlowableOnSubscribe<BaseResponseJson>() {
            @Override
            public void subscribe(FlowableEmitter<BaseResponseJson> baseResponseJson) throws Exception {
                try {
                    Response baseResponseJsonResponse = rxReServer.sendRequest(url, userRequest).execute();
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

    /**
     * 标准 rxjava2  retrofit2
     * @param userId
     * @param password
     * @return
     */
    public static Observable<UserInfo>  login(String userId, String password) {

        return rxReServer.login(userId, password);
    }


}

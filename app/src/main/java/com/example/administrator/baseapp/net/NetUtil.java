package com.example.administrator.baseapp.net;


import com.example.administrator.baseapp.constants.NetConstants;
import com.example.administrator.baseapp.net.request.BaseRequestJson;
import com.example.administrator.baseapp.net.request.LoginRequest;
import com.example.administrator.baseapp.net.response.BaseResponseJson;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beini on 2017/2/10.
 */

public class NetUtil<T> {
    public static NetUtil instance;

    public static NetUtil getSingleton() {
        if (instance == null) {
            synchronized (NetUtil.class) {
                if (instance == null) {
                    instance = new NetUtil();
                }
            }
        }
        return instance;
    }

    public Call<BaseResponseJson> sendRequestGet(String url, BaseRequestJson baseRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServer apiServer = retrofit.create(ApiServer.class);

        Call<BaseResponseJson> call = apiServer.sendRequestGet(url, baseRequest);
        return call;
    }

    public Call<BaseResponseJson> sendRequestPost(String url, BaseRequestJson baseRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServer apiServer = retrofit.create(ApiServer.class);

        Call<BaseResponseJson> call = apiServer.sendRequestPost(url, baseRequest);
        return call;
    }

    public Call<BaseResponseJson> sendRequestPost1(String url, T baseRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServer apiServer = retrofit.create(ApiServer.class);

        Call<BaseResponseJson> call = apiServer.sendRequestPost1(url, baseRequest);
        return call;
    }
}

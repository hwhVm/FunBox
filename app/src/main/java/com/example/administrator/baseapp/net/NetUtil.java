package com.example.administrator.baseapp.net;


import com.example.administrator.baseapp.net.request.BaseRequestJson;
import com.example.administrator.baseapp.net.request.LoginRequest;
import com.example.administrator.baseapp.net.response.BaseResponseJson;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beini on 2017/2/10.
 */

public class NetUtil {
    private static String baseUrl = "http://10.0.0.67:8080/SpringMVC/";

    public static Call<BaseResponseJson> sendRequestGet(String url, BaseRequestJson baseRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServer apiServer = retrofit.create(ApiServer.class);

        Call<BaseResponseJson> call = apiServer.sendRequestGet(url, baseRequest);
        return call;
    }

    public static Call<BaseResponseJson> sendRequestPost(String url, BaseRequestJson baseRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServer apiServer = retrofit.create(ApiServer.class);

        Call<BaseResponseJson> call = apiServer.sendRequestPost(url, baseRequest);
        return call;
    }

    public static Call<BaseResponseJson> sendRequestPost1(String url, LoginRequest baseRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServer apiServer = retrofit.create(ApiServer.class);

        Call<BaseResponseJson> call = apiServer.sendRequestPost1(url, baseRequest);
        return call;
    }
}

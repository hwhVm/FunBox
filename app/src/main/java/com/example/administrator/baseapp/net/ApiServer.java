package com.example.administrator.baseapp.net;

import com.example.administrator.baseapp.net.request.BaseRequestJson;
import com.example.administrator.baseapp.net.request.LoginRequest;
import com.example.administrator.baseapp.net.response.BaseResponseJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by beini on 2017/2/10.
 */

public interface ApiServer {
    @GET("{url}")
    Call<BaseResponseJson> sendRequestGet(@Path("url") String url, @Body BaseRequestJson baseRequestJson);

    @POST("{url}")
    Call<BaseResponseJson> sendRequestPost(@Path("url") String url, @Body BaseRequestJson baseRequestJson);

    @POST("{url}")
    Call<BaseResponseJson> sendRequestPost1(@Path("url") String url, @Body LoginRequest baseRequestJson);
}

package com.example.administrator.baseapp.net;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by beini on 2017/2/10.
 */

public interface GitHubService {
    @GET("/")
    Call<String> getData();
}

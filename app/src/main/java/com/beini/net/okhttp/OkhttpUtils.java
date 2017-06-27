package com.beini.net.okhttp;

import android.os.Environment;

import com.beini.constants.NetConstants;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by beini on 2017/4/21.
 */

public class OkhttpUtils {
    private static OkhttpUtils ourInstance = new OkhttpUtils();

    public static OkhttpUtils getInstance() {
        return ourInstance;
    }

    OkHttpClient client;

    public OkhttpUtils() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        File file = new File(Environment.getDataDirectory() + "/cacheBeini/");
        Cache cacheBeini = new Cache(file, cacheSize);
        if (!file.exists()) {
            file.mkdir();
        }
        client = new OkHttpClient
                .Builder()
                .readTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .cache(cacheBeini)//设置缓存
                //	proxy(proxy).//设置代理
                //	authenticator(authenticator).
                .build();
    }

    public Call requestDataGet(String url) {
        Request request = new Request.Builder()
                .url(NetConstants.baseUrl + url)
                .build();

        return client.newCall(request);
    }

}

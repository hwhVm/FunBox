package com.example.administrator.baseapp.base;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;

import com.example.administrator.baseapp.ndk.NDKMain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by beini on 2017/2/8.
 */

public class BaseApplication extends Application {

    private static BaseApplication application;
    private ExecutorService executorService;
    private NDKMain ndk;

    public static BaseApplication getInstance() {
        if (application == null) {
            application = new BaseApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        executorService = Executors.newCachedThreadPool();//创建线程池
        ndk = new NDKMain();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
    /**
     * 获取ndk对象
     */
    public NDKMain getNdk() {
        return ndk;
    }
}

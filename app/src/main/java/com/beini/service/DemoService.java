package com.beini.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.beini.utils.BLog;


/**
 * Created by beini on 2017/3/18.
 */

public class DemoService extends Service {

    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {

        public DemoService getService() {
            return DemoService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {//在服务第一次创建的时候调用
        BLog.d("  DemoService  onCreate ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {// 每次服务启动的时候调用
        BLog.d("    DemoService  onStartCommand  ");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {// 当服务销毁时
        BLog.d("    DemoService  onDestroy");
        super.onDestroy();
    }
}

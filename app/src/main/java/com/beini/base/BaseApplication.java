package com.beini.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.beini.ndk.NDKMain;
import com.beini.utils.CrashHandler;
import com.beini.utils.SystemUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by beini on 2017/2/8.
 */

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication application;
    private ExecutorService executorService;
    private NDKMain ndk;

    public static BaseApplication getInstance() {
        if (application == null) {
            application = new BaseApplication();
        }
        return application;
    }

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        String appName = getApplicationInfo().packageName;
        application = this;
        if (appName.equals(SystemUtil.getCurProcessName(getApplicationContext()))) {//避免多进程下会重复执行
            executorService = Executors.newCachedThreadPool();//创建线程池
            ndk = new NDKMain();
            Fresco.initialize(getInstance());
            CrashHandler.getInstance().init(getApplicationContext());
//            Stetho.initializeWithDefaults(this);
        }
//        搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        //非wifi是否下载内核
//        QbSdk.setDownloadWithoutWifi(true);
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

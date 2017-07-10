package com.beini.app;

import android.support.multidex.MultiDexApplication;

import com.beini.ndk.NDKMain;
import com.beini.util.CrashHandler;
import com.beini.util.SystemUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.smtt.sdk.QbSdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by beini on 2017/2/8.
 */

public class GlobalApplication extends MultiDexApplication {

    private static GlobalApplication application;
    private ExecutorService executorService;
    private NDKMain ndk;
    private final String TAG = "GlobalApplication";

    public static GlobalApplication getInstance() {
        if (application == null) {
            application = new GlobalApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//       Debug.startMethodTracing("GlobalApplication");//生成分析文件，对冷启动时间进行分析
        application = this;
        String appName = getApplicationInfo().packageName;
        if (appName.equals(SystemUtil.getCurProcessName(getApplicationContext()))) {//避免多进程下会重复执行
            executorService = Executors.newCachedThreadPool();//创建线程池
            ndk = new NDKMain();
//          Stetho.initializeWithDefaults(this);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Fresco.initialize(getInstance());
                    initTbs();
                    CrashHandler.getInstance().init(getApplicationContext());
                }
            });
        }
    }

    /**
     * 腾讯x5浏览器初始化,非常占用cup和启动时间
     */
    private void initTbs() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
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

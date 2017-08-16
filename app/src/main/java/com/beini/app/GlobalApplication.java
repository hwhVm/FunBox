package com.beini.app;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.beini.ndk.NDKMain;
import com.beini.util.BLog;
import com.beini.util.SystemUtil;
import com.beini.util.listener.ActivityCallbacks;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by beini on 2017/2/8.
 */

public class GlobalApplication extends MultiDexApplication {

    private static GlobalApplication application;
    private ExecutorService executorService;
    private NDKMain ndk;
    private Activity curActivity = null;//当前栈顶的activity
    private List<Activity> activities;

    public static GlobalApplication getInstance() {

        return application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//       Debug.startMethodTracing("GlobalApplication");//生成分析文件，对冷启动时间进行分析
        String appName = getApplicationInfo().packageName;
        if (appName.equals(SystemUtil.getCurProcessName(getApplicationContext()))) {//避免多进程下会重复执行
            activities = new ArrayList<>();
            application = this;
            this.registerActivityLifecycleCallbacks(new ActivityCallbacks());
            executorService = Executors.newCachedThreadPool();//创建线程池
            ndk = new NDKMain();
            initBugly();
//          Stetho.initializeWithDefaults(this);
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    Fresco.initialize(getInstance());
//                    initTbs();
//                    CrashHandler.getInstance().init(getApplicationContext());
                }
            });
        }
    }

    private void initBugly() {
        //Bugly会在启动10s后联网同步数据。若您有特别需求，可以修改这个时间。
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppReportDelay(20000);   //改为20s
        //为了保证运营数据的准确性，建议不要在异步线程初始化Bugly。
        Bugly.init(getApplicationContext(), "2acd33499a", true,strategy);
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

    @Override
    public void onLowMemory() {
        BLog.e(" -------------->onLowMemory");
        super.onLowMemory();
    }

    /**
     * get set
     */
    public Activity getCurActivity() {
        return curActivity;
    }

    public void setCurActivity(Activity curActivity) {
        this.curActivity = curActivity;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void removeList(Activity activity) {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getClass().getName().equals(activity.getClass().getName())) {
                activities.remove(i);
            }
        }
    }
}

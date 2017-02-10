package com.example.administrator.baseapp.base;

import android.app.Application;

/**
 * Created by beini on 2017/2/8.
 */

public class BaseApplication extends Application {

    private static BaseApplication application;


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
    }
}

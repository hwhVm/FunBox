package com.beini.util.listener;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.beini.app.GlobalApplication;

/**
 * Created by beini on 2017/7/10.
 */

public class ActivityCallbacks implements Application.ActivityLifecycleCallbacks {

    private final String TAG = "ActivityCallbacks";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//        BLog.e(TAG, "  onActivityCreated " + activity.getClass().getName());
        GlobalApplication.getInstance().getActivities().add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
//        BLog.e(TAG, "  onActivityStarted " + activity.getClass().getName());
        GlobalApplication.getInstance().setCurActivity(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        BLog.e(TAG, "  onActivityResumed " + activity.getClass().getName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        BLog.e(TAG, "  onActivityPaused " + activity.getClass().getName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
//        BLog.e(TAG, "  onActivityStopped " + activity.getClass().getName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        BLog.e(TAG, "  onActivitySaveInstanceState " + activity.getClass().getName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//        BLog.e(TAG, "  onActivityDestroyed " + activity.getClass().getName());
        GlobalApplication.getInstance().removeList(activity);
    }


}

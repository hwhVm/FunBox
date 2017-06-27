package com.beini.util.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/4/24.
 * 需要在application注册 ：registerActivityLifecycleCallbacks(new ActivityCallbacks());
 */
public class ActivityCallbacks implements Application.ActivityLifecycleCallbacks {

    private static boolean isBackground = false;
    private static Activity mActivity;
    private List<Activity> aList = new ArrayList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        aList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        mActivity = activity;
        isBackground = false;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        isBackground = false;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (mActivity == activity) {
            isBackground = true;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mActivity == activity) {
            isBackground = true;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * get set
     */
    public static boolean isIsBackground() {
        return isBackground;
    }

    public static void setIsBackground(boolean isBackground) {
        ActivityCallbacks.isBackground = isBackground;
    }

    public static Activity getmActivity() {
        return mActivity;
    }

    public static void setmActivity(Activity mActivity) {
        ActivityCallbacks.mActivity = mActivity;
    }

    public List<Activity> getaList() {
        return aList;
    }

    public void setaList(List<Activity> aList) {
        this.aList = aList;
    }
}

package com.beini.utils.activity;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

/**
 * Created by beini on 2017/4/24.
 * 弱引用 Activity 栈管理
 */

public class ActivityStackManager {


    public ActivityStackManager() {

    }

    private Stack<WeakReference<Activity>> mWeakReferences;

    private static ActivityStackManager activityStackManager = new ActivityStackManager();


    public static ActivityStackManager getInstance() {
        if (activityStackManager == null) {
            activityStackManager = new ActivityStackManager();
        }
        return activityStackManager;
    }

    public int stackSize() {
        return mWeakReferences.size();
    }

    public Stack<WeakReference<Activity>> getmWeakReferences;

    public void setmWeakReferences(Stack<WeakReference<Activity>> mWeakReferences) {
        this.mWeakReferences = mWeakReferences;
    }

    public void addActivity(WeakReference<Activity> activityWeakReference) {
        if (mWeakReferences == null) {
            mWeakReferences = new Stack<>();
        }
    }

    public void removeActivity(WeakReference<Activity> activityWeakReference) {
        if (mWeakReferences != null) {
            mWeakReferences.remove(activityWeakReference);
        }
    }

    public Activity getTopActivity() {
        Activity activity = mWeakReferences.lastElement().get();
        if (null == activity) {
            return null;
        } else {
            return mWeakReferences.lastElement().get();
        }
    }

    public Activity getActivityByClass(Class<?> cls) {
        Activity return_acitvity = null;
        for (WeakReference<Activity> activityWeakReference : mWeakReferences) {
            if (activityWeakReference.get().getClass().equals(cls)) {
                return_acitvity = activityWeakReference.get();
                break;
            }
        }
        return return_acitvity;
    }

    public void killTopActivity() {
        WeakReference<Activity> activityWeakReference = mWeakReferences.lastElement();
        killActivity(activityWeakReference);
    }

    public void killActivity(WeakReference<Activity> activity) {
        try {
            Iterator<WeakReference<Activity>> iterator = mWeakReferences.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> stackActivity = iterator.next();
                if (stackActivity.get() == null) {
                    iterator.remove();
                    continue;
                }
                if (stackActivity.get().getClass().getName().equals(activity.get().getClass().getName())) {
                    iterator.remove();
                    stackActivity.get().finish();
                    break;
                }
            }
        } catch (Exception e) {

        }
    }

    public void killActivity(Class<?> cls) {
        try {
            ListIterator<WeakReference<Activity>> listIterator = mWeakReferences.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity == null) {
                    listIterator.remove();
                    continue;
                }
                if (activity.getClass() == cls) {
                    listIterator.remove();
                    if (activity != null) {
                        activity.finish();
                    }
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void killAllActivity() {
        try {
            ListIterator<WeakReference<Activity>> listIterator = mWeakReferences.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null) {
                    activity.finish();
                }
                listIterator.remove();
            }
        } catch (Exception e) {
        }
    }


    public void killAllActivityExceptOne(Class cls) {
        try {
            for (int i = 0; i < mWeakReferences.size(); i++) {
                WeakReference<Activity> activity = mWeakReferences.get(i);
                if (activity.getClass().equals(cls)) {
                    break;
                }
                if (mWeakReferences.get(i) != null) {
                    killActivity(activity);
                }
            }
        } catch (Exception e) {
        }
    }

    public void AppExit(Context context) {
        killAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

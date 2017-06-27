package com.beini.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by beini on 2017/3/10.
 */

public class SystemUtil {

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * APP是否在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : processInfos) {
            if (packageName.equals(appProcess.processName)) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {//前台

                } else {//后台

                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}

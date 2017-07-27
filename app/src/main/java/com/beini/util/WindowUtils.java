package com.beini.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.beini.app.GlobalApplication;

/**
 * Created by beini on 2017/3/15.
 */

public class WindowUtils {
    /**
     * 隐藏虚拟按键
     */
    public static void setHide(View view) {
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    /**
     * 返回屏幕密度,来获取屏幕高度和宽度
     */
    public static DisplayMetrics returnDM() {
        //获取屏幕高度宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) GlobalApplication.getInstance().getCurActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(dm);
        return dm;

    }
}

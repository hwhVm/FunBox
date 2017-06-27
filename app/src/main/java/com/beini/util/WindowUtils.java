package com.beini.util;

import android.view.View;

/**
 * Created by beini on 2017/3/15.
 */

public class WindowUtils {
    /**
     * 隐藏虚拟按键
     * */
    public static void setHide(View view){
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}

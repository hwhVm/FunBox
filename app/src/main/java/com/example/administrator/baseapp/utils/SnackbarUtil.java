package com.example.administrator.baseapp.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;


/**
 * Toast统一管理类
 */
public class SnackbarUtil {

    private SnackbarUtil() {

        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;


    public static void showShort(View view, CharSequence message) {
        if (isShow)
            Snackbar.make(view, message, Toast.LENGTH_SHORT).show();
    }


    public static void showShort(View view, int message) {
        if (isShow)
            Snackbar.make(view, message, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(View view, CharSequence message) {
        if (isShow)
            Snackbar.make(view, message, Toast.LENGTH_LONG).show();
    }


    public static void showLong(View view, int message) {
        if (isShow)
            Snackbar.make(view, message, Toast.LENGTH_LONG).show();
    }

    public static void show(View view, CharSequence message, int duration) {
        if (isShow)
            Snackbar.make(view, message, duration).show();
    }

    public static void show(View view, int message, int duration) {
        if (isShow)
            Snackbar.make(view, message, duration).show();
    }

}
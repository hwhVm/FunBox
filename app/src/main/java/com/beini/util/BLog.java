package com.beini.util;

import android.util.Log;

import com.beini.constants.Constants;


/**
 * Created by beini on 17/02/09.
 */
public class BLog {

    private static String TAG = "com.beini";


    public static void d(String msg) {
        if (Constants.DEBUG) {
            Log.d(TAG, "-------------------------------------->" + msg);
        }

    }

    public static void e(String msg) {
        if (Constants.DEBUG) {
            Log.e(TAG, "-------------------------------------->" + msg);
        }

    }

    public static void d(String customTag, String msg) {
        if (Constants.DEBUG) {
            Log.e(customTag, "-------------------------------------->" + msg);
        }
    }

    public static void e(String customTag, String msg) {
        if (Constants.DEBUG) {
            Log.e(customTag, "-------------------------------------->" + msg);
        }

    }
}

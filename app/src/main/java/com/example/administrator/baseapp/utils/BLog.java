package com.example.administrator.baseapp.utils;

import android.util.Log;

import com.example.administrator.baseapp.constants.Constants;

/**
 * Created by beini on 17-02-09.
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


}

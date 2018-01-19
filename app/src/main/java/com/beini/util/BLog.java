package com.beini.util;

import android.util.Log;

import com.beini.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

    public static void json(String json) {
        if (json == null || json.length() == 0) {
            e("json is Empty");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(2);
                d(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(2);
                d(message);
                return;
            }
            e("Invalid Json");
        } catch (JSONException e) {
            e("Invalid Json");
        }
    }

}

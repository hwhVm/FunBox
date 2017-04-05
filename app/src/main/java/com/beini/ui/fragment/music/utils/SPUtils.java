package com.beini.ui.fragment.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.beini.base.BaseApplication;
import com.beini.constants.Constants;

/**
 * Created by bieni on 2017/2/20.
 */

public class SPUtils {


    public static final String TAG = "octopus.SPUtils";


    private static SharedPreferences getSharedPreferences() {
        return BaseApplication.getInstance().getSharedPreferences("baseApp", Context.MODE_PRIVATE);
    }

    public static void SavePlaySong(String Song){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(Constants.SP_DLNA_SONG, Song);
        editor.apply();
    }

    public static String getPlaySong(){
        return getSharedPreferences().getString(Constants.SP_DLNA_SONG, "");
    }

}
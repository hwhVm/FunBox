package com.beini.constants;

import com.beini.app.GlobalApplication;

/**
 * Created by beini on 2017/2/9.
 */

public class Constants {

    public static Boolean DEBUG = true;

    public static final String URL_ALL_FILE = GlobalApplication.getInstance().getExternalCacheDir().getAbsolutePath();//  /storage/emulated/0/Android/data/com.beini/cache

    public static final int HOME_CPP = 0;
    public static final int HOME_SHAKE = 1;
    public static final int HOME_SIDESLIP = 2;
    //MediaFunction
    public static final int HOME_MEDIAFUNCTION = 3;
    public static final int HOME_SPP = 4;
    public static final int HOME_SMS = 5;
    //Notification
    public static final int HOME_NOTIFICATION = 6;
    //Broadcast
    public static final int HOME_BROADCAST = 7;
    //Ani
    public static final int HOME_ANI = 8;
    public static final int HOME_AIDL = 9;
    public static final int HOME_SERVICE = 10;
    public static final int HOME_POPUPWINDOW = 11;
    public static final int HOME_SNAP = 12;
    public static final int HOME_TAG = 13;
    public static final int HOME_CANVAS = 14;
    public static final int HOME_FMOD = 15;
    public static final int HOME_COORDINATOR = 16;
    public static final int HOME_BEAT = 17;
    public static final int HOME_CIRCLEANI = 18;
    public static final int HOME_ANI_FADE = 19;
    public static final int HOME_DISKLRU = 20;
    public static final int HOME_CUT = 21;
    public static final int HOME_OTHER = 22;
    public static final int HOME_SENSOR= 23;
    public static final int HOME_VIEWPAGE= 24;
    public static final int HOME_NET= 25;


}

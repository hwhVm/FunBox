package com.beini.ndk;

public class NDKMain {
    static {
        System.loadLibrary("BaseProject");
    }
    public native String stringFromJNI();
}

package com.beini.ndk;

public class NDKMain {
    static {
        System.loadLibrary("FunBox");
    }

    public native String getPassword();

    public native String withArgs(String args);
}

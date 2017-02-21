package com.example.administrator.baseapp.ndk;


public class NDKMain {
    static {
        System.loadLibrary("Ndk");
    }
    public native byte[] variations(byte[] data,float one,float two,float three);

}

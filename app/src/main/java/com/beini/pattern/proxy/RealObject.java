package com.beini.pattern.proxy;

/**
 * Created by beini on 2017/3/1.
 */

public class RealObject implements Subject {

    @Override
    public void request(String string) {
        System.out.println("  string="+string);
    }
}

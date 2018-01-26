package com.beini.ndk;

public class NDKMain {
    static {
        System.loadLibrary("FunBox");
    }

    /**
     * 非静态方法
     * jobject 代表native方法所属的对象。
     */
    public native String getPassword();

    public native String withArgs(String args);

    /**
     * 静态方法
     * jclass 代表native方法所属类的class对象(NDKMain.class)
     */

    /**
     * 访问Java类的非静态属性
     */
    public String str = "hello beini";

    //访问非静态属性str，修改它的值
    public native void accessField();

    /**
     * 访问Java类的静态属性
     */
    public int num = 666;
    public native void accessStaticField();

//    访问Java类的非静态方法。
//    访问Java类的静态方法。
//    间接访问Java类的父类的方法。
//    访问Java类的构造方法。

}

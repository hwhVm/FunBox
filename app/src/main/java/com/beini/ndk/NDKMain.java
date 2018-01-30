package com.beini.ndk;

import com.beini.ui.fragment.cpptest.Human;
import com.beini.ui.fragment.cpptest.Man;
import com.beini.util.BLog;

import java.util.Random;
import java.util.UUID;

public class NDKMain {
    static {
        System.loadLibrary("FunBox");
        System.loadLibrary("fmod-lib");
    }

    /**
     * 非静态方法
     * jobject(jclass) 代表native方法所属的对象。
     */
    public native String getPassword();

    public native String withArgs(String args);

    /**
     * 静态方法
     * jobject(jclass) 代表native方法所属类的class对象(NDKMain.class)
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
    public static int num = 666;

    public native void accessStaticField();

    /**
     * 访问Java类的非静态方法
     */
    public int getRandom(int max) {
        BLog.e("getRandom");
        return new Random().nextInt(max);
    }

    public native void accessMethod();

    /**
     * 访问Java类的静态方法
     */
    public static String getUUID() {
        BLog.e("getUUID");
        return UUID.randomUUID().toString();
    }

    public native void accessStaticMethod();

    /**
     * 间接访问Java类的父类的方法
     */
    //父类的引用指向子类
    Human man = new Man();

    public native void accessNonvirtualMethod();

    /**
     * 访问Java类的构造方法
     * 调用Date的构造函数
     */
    public native long accessConstructor();

    /**
     * JNI返回中文乱码问题
     */
    public native void testChineseIn(String chinese);//传进去

    public native String testChineseOut();//取出来会乱码--我打印不会

    /**
     * 数组的处理
     */
    public native void sortArray(int[] sorArry);

    /**
     * jni引用
     */
    //局部引用
    public native void localRef();

    /**
     * 全局引用
     * 在一个native方法被多次调用之间，可以使用一个全局引用跨越它们。一个全局引用可以跨越多个线程，并且在被程序员释放之前，一致有效。和局部引用一样，全局引用保证了所引用的对象不会被垃圾回收。
     */
    //创建
    public native void createGlobalRef();

    //获取
    public native String getGlobalRef();

    //删除
    public native void deteleGlobalRef();

    /**
     * 自己写的c文件测试
     */
    public native void getCmethod();

    /**
     * 声音
     */
    //调用fmod去播放音乐
    public native void playsMusic(String music_path);
}

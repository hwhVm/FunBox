package com.example.administrator.baseapp.pattern.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ProxyTest {
    public static void main(String[] args) {
        System.out.println("main");
        ProxyHandler handler = new ProxyHandler();
        Subject proxy = (Subject) Proxy.newProxyInstance(RealObject.class.getClassLoader()
                , RealObject.class.getInterfaces(), handler);
        proxy.request("ff");
    }
}

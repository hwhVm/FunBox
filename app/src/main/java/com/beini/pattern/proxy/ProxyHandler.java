package com.beini.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ProxyHandler implements InvocationHandler {
    private Subject subject;

    public ProxyHandler() {
        System.out.println("ProxyHandler");
        subject = new RealObject();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke");
        Object result = method.invoke(subject, args);
        return result;
    }
}

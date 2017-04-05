package com.beini.pattern.flyweight;

/**
 * Created by beini on 2017/3/9.
 */

public class ConcreteFlyweight implements Flyweight {


    private String intrinsicState = null;

    /**
     * 构造函数 内蕴状态作为参数传入
     */
    public ConcreteFlyweight(String _intrinsicState) {
        this.intrinsicState = _intrinsicState;
    }

    @Override
    public void operation(String state) {
        System.out.println("内蕴状态：" + intrinsicState);
        System.out.println("外蕴状态：" + state);
    }
}

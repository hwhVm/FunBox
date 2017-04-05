package com.beini.pattern.flyweight;

/**
 * Created by beini on 2017/3/9.
 */

public class TestFlyWeight {
    public static void main(String[] args) {
//        FlyweightFactory flyweightFactory = new FlyweightFactory();
//        ConcreteFlyweight concreteFlyweight1 = (ConcreteFlyweight) flyweightFactory.factory("aaaaa");
//        ConcreteFlyweight concreteFlyweight2 = (ConcreteFlyweight) flyweightFactory.factory("dddddd");
//
//        concreteFlyweight1.operation("1111111");
//        concreteFlyweight2.operation("22222222");
        Integer a = 120;
        Integer b =120;
        System.out.println("   " + (a == b));
    }
}

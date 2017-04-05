package com.beini.pattern.strategy;

/**
 * Created by beini on 2017/3/9.
 */

public class StrategyTest {
    public static void main(String[] args) {
        Context context = new Context(new AddStrategy());
        System.out.println(context.calculate(2, 3));
    }
}

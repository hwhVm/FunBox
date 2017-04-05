package com.beini.pattern.strategy;

/**
 * Created by beini on 2017/3/9.
 */

public class AddStrategy implements Strategy {
    @Override
    public int calculate(int a, int b) {
        return a+b;
    }
}

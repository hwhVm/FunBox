package com.example.administrator.baseapp.pattern.strategy;

/**
 * Created by Administrator on 2017/3/9.
 */

public class SubstractStrategy implements Strategy {
    @Override
    public int calculate(int a, int b) {
        return a-b;
    }
}

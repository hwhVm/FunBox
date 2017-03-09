package com.example.administrator.baseapp.pattern.strategy;

/**
 * Created by Administrator on 2017/3/9.
 */

public class Context {
    private Strategy strategy;

    public Context(Strategy strategy) {
        super();
        this.strategy = strategy;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public int calculate(int a, int b) {
        return strategy.calculate(a, b);
    }
}

package com.example.administrator.baseapp.pattern.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by beini on 2017/3/9.
 */

public class FlyweightFactory {
    private Map<Integer, Flyweight> labels = new HashMap<Integer, Flyweight>();

    public Flyweight factory(String intrinsicState) {

        int hashCode = intrinsicState.hashCode();

        Flyweight fly = labels.get(hashCode);
        if (fly == null) {
            fly = new ConcreteFlyweight(intrinsicState);
            labels.put(hashCode, fly);
        }
        return fly;
    }
}

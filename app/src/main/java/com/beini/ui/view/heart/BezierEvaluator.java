package com.beini.ui.view.heart;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by beini on 2017/10/18.
 * 加速度 ，控制快慢
 */

public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF pointF1;
    private PointF pointF2;

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float timeLeft = 1.0f - fraction;
        PointF point = new PointF();// 结果

        point.x = timeLeft * timeLeft * timeLeft * (startValue.x) + 3
                * timeLeft * timeLeft * fraction * (pointF1.x) + 3 * timeLeft
                * fraction * fraction * (pointF2.x) + fraction * fraction * fraction * (endValue.x);

        point.y = timeLeft * timeLeft * timeLeft * (startValue.y) + 3
                * timeLeft * timeLeft * fraction * (pointF1.y) + 3 * timeLeft
                * fraction * fraction * (pointF2.y) + fraction * fraction * fraction * (endValue.y);
        return point;
    }
}

package com.beini.animation;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by beini on 2017/10/18.
 * 抖动
 * usage:
 * ShakeAnimation myAnimation = new ShakeAnimation();
 * myAnimation.setDuration(1000);
 * myAnimation.setRepeatCount(1);
 * frame_layout_whole.startAnimation(myAnimation);
 * layout_periscope.addHeart();
 */

public class ShakeAnimation extends Animation {

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    /**
     * @param interpolatedTime :表示当前动画进行的时间与动画总时间（一般在 setDuration() 方法中设置）的比值，从0逐渐增大到1；
     * @param t                :传递当前动画对象，一般可以通过代码 android.graphics.Matrix matrix = t.getMatrix() 获得 Matrix 矩阵对象，再设置 Matrix 对象，一般要用到 interpolatedTime 参数，以此达到控制动画实现的结果。
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Log.e("com.beini", "  interpolatedTime=" + interpolatedTime);
        t.getMatrix().setTranslate(
                (float) Math.sin(interpolatedTime * 50) * 8,
                (float) Math.sin(interpolatedTime * 50) * 8
        );// 50越大频率越高，8越小振幅越小
//        t.getMatrix().setScale(-1,-1);

        super.applyTransformation(interpolatedTime, t);
    }
}

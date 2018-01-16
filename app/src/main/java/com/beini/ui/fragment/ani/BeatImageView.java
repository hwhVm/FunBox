package com.beini.ui.fragment.ani;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.beini.util.BLog;

/**
 * Created by beini on 2018/1/16.
 */

public class BeatImageView extends android.support.v7.widget.AppCompatImageView {
    private float scale = 1f;

    public BeatImageView(Context context) {
        super(context);
    }

    public BeatImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BeatImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setScaleX(scale);
        setScaleY(scale);
    }


    private ValueAnimator mAnimator;

    public void startAnimation() {
        mAnimator = ValueAnimator.ofFloat(1, 2);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (float) animation.getAnimatedValue();
                BLog.e("scale=" + scale);
                postInvalidate();
            }
        });
        // 重复次数 -1表示无限循环
        mAnimator.setRepeatCount(-1);
        // 重复模式, RESTART: 重新开始 REVERSE:恢复初始状态再开始
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 关闭动画
        mAnimator.end();
    }
}

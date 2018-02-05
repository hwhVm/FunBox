package com.beini.ui.fragment.ani;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.util.BLog;


/**
 * Create by beini 2017/8/17
 */
@ContentView(R.layout.fragment_ani)
public class AniFragment extends BaseFragment {
    @ViewInject(R.id.frontView)
    LinearLayout frontView;
    @ViewInject(R.id.bottom)
    LinearLayout bottomView;

    private AnimatorSet showAnim, hiddenAnim;
    private long fWidth, fHeight, bHeight;

    @Override
    public void initView() {
        ViewTreeObserver vto = frontView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BLog.d("   onGlobalLayout  frontView");
                fWidth = frontView.getMeasuredWidth();
                fHeight = frontView.getMeasuredHeight();

            }
        });

        ViewTreeObserver sVto = bottomView.getViewTreeObserver();
        sVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bHeight = bottomView.getMeasuredHeight();
                BLog.d("   onGlobalLayout  bottomView");
                initShowAnim();
                initHiddenAnim();
            }
        });
    }

    private boolean tag = false;

    @Event({R.id.btn_showa})
    private void mEvent(View view) {
        if (tag) {
            hiddenAnim.start();
            tag = false;
        } else {
            showAnim.start();
            tag = true;
        }

    }

    private void initShowAnim() {
        ObjectAnimator fViewScaleXAnim = ObjectAnimator.ofFloat(frontView, "scaleX", 1.0f, 0.8f);
        fViewScaleXAnim.setDuration(350);
        ObjectAnimator fViewScaleYAnim = ObjectAnimator.ofFloat(frontView, "scaleY", 1.0f, 0.8f);
        fViewScaleYAnim.setDuration(350);
        ObjectAnimator fViewAlphaAnim = ObjectAnimator.ofFloat(frontView, "alpha", 1.0f, 0.5f);
        fViewAlphaAnim.setDuration(350);
        ObjectAnimator fViewRotationXAnim = ObjectAnimator.ofFloat(frontView, "rotationX", 0f, 10f);
        fViewRotationXAnim.setDuration(200);
        ObjectAnimator fViewResumeAnim = ObjectAnimator.ofFloat(frontView, "rotationX", 10f, 0f);
        fViewResumeAnim.setDuration(150);
        fViewResumeAnim.setStartDelay(200);
        ObjectAnimator fViewTransYAnim = ObjectAnimator.ofFloat(frontView, "translationY", 0, -0.1f * fHeight);
        fViewTransYAnim.setDuration(350);
        ObjectAnimator sViewTransYAnim = ObjectAnimator.ofFloat(bottomView, "translationY", bHeight, 0);
        sViewTransYAnim.setDuration(350);
        sViewTransYAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                bottomView.setVisibility(View.VISIBLE);
            }
        });
        showAnim = new AnimatorSet();
        showAnim.playTogether(fViewScaleXAnim, fViewRotationXAnim, fViewResumeAnim, fViewTransYAnim, fViewAlphaAnim, fViewScaleYAnim, sViewTransYAnim);
    }

    private void initHiddenAnim() {
        ObjectAnimator fViewScaleXAnim = ObjectAnimator.ofFloat(frontView, "scaleX", 0.8f, 1.0f);
        fViewScaleXAnim.setDuration(350);
        ObjectAnimator fViewScaleYAnim = ObjectAnimator.ofFloat(frontView, "scaleY", 0.8f, 1.0f);
        fViewScaleYAnim.setDuration(350);
        ObjectAnimator fViewAlphaAnim = ObjectAnimator.ofFloat(frontView, "alpha", 0.5f, 1.0f);
        fViewAlphaAnim.setDuration(350);
        ObjectAnimator fViewRotationAnim = ObjectAnimator.ofFloat(frontView, "rotationX", 0f, 10f);
        fViewRotationAnim.setDuration(150);
        ObjectAnimator fViewResumeAnim = ObjectAnimator.ofFloat(frontView, "rotationX", 10f, 0f);
        fViewResumeAnim.setDuration(200);
        fViewResumeAnim.setStartDelay(150);
        ObjectAnimator fViewTransYAnim = ObjectAnimator.ofFloat(frontView, "translationY", -fHeight * 0.1f, 0);
        fViewTransYAnim.setDuration(350);
        ObjectAnimator sViewTransYAnim = ObjectAnimator.ofFloat(bottomView, "translationY", 0, bHeight);
        sViewTransYAnim.setDuration(350);
        sViewTransYAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bottomView.setVisibility(View.INVISIBLE);
            }
        });
        hiddenAnim = new AnimatorSet();
        hiddenAnim.playTogether(fViewScaleXAnim, fViewAlphaAnim, fViewRotationAnim, fViewResumeAnim, fViewScaleYAnim, fViewTransYAnim, sViewTransYAnim);
        hiddenAnim.setDuration(350);
    }

    /**
     * 平移
     */
    public void translation(    View customerView) {
        ObjectAnimator objectAnimatort1 = ObjectAnimator.ofFloat(customerView, "translationX", 0, 300);
        ObjectAnimator objectAnimatort2 = ObjectAnimator.ofFloat(customerView, "translationY", 0, 300);
        ObjectAnimator objectAnimators1 = ObjectAnimator.ofFloat(customerView, "scaleX", 1f, 2f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(4000);
        animatorSet.play(objectAnimatort1).with(objectAnimatort2).after(objectAnimators1);
        animatorSet.start();
    }

    public void valueAnimatorMethod(View customerView) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 300).setDuration(2000);
        valueAnimator.setTarget(customerView);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                Log.e("com.beini", " value=" + value);
            }
        });
    }
}
